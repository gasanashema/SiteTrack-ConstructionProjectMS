package service.implementation;

import dao.MaterialUsageDao;
import dao.ProjectMaterialStockDao;
import dao.MaterialStockMovementDao;
import dao.ProjectDao;
import dao.MaterialDao;
import dao.UserDao;
import dto.MaterialUsageDTO;
import model.MaterialUsage;
import model.ProjectMaterialStock;
import model.MaterialStockMovement;
import model.Project;
import model.Material;
import model.User;
import model.EMovementType;
import service.interfaces.MaterialUsageService;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MaterialUsageServiceImpl extends UnicastRemoteObject implements MaterialUsageService {
    private final MaterialUsageDao dao;
    private final ProjectMaterialStockDao stockDao;
    private final MaterialStockMovementDao movementDao;
    private final ProjectDao projectDao;
    private final MaterialDao materialDao;
    private final UserDao userDao;

    public MaterialUsageServiceImpl() throws RemoteException {
        super();
        this.dao = new MaterialUsageDao();
        this.stockDao = new ProjectMaterialStockDao();
        this.movementDao = new MaterialStockMovementDao();
        this.projectDao = new ProjectDao();
        this.materialDao = new MaterialDao();
        this.userDao = new UserDao();
    }

    private MaterialUsageDTO toDTO(MaterialUsage entity) {
        if (entity == null) return null;
        return new MaterialUsageDTO(
            entity.getId(),
            entity.getProject() != null ? entity.getProject().getId() : null,
            entity.getProject() != null ? entity.getProject().getProjectName() : null,
            entity.getMaterial() != null ? entity.getMaterial().getId() : null,
            entity.getMaterial() != null ? entity.getMaterial().getMaterialName() : null,
            entity.getMaterial() != null ? entity.getMaterial().getUnit() : null,
            entity.getQuantityUsed(),
            entity.getUnitPrice(),
            entity.getTotalCost(),
            entity.getUsageDate(),
            entity.getActivityDescription(),
            entity.getRecordedBy() != null ? entity.getRecordedBy().getFullName() : null
        );
    }

    @Override
    public MaterialUsageDTO recordUsage(MaterialUsageDTO dto) throws RemoteException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Project p = projectDao.findById(dto.getProjectId());
            Material m = materialDao.findById(dto.getMaterialId());
            User u = userDao.findById(dto.getRecordedByName()); 

            if (p == null || m == null || u == null) throw new IllegalArgumentException("Project, Material, or User not found");

            ProjectMaterialStock stock = stockDao.findByProjectAndMaterial(p.getId(), m.getId());
            if (stock == null || stock.getQuantityAvailable().compareTo(dto.getQuantityUsed()) < 0) {
                throw new IllegalArgumentException("Insufficient stock available for material");
            }

            // 1. Create Usage Record
            MaterialUsage usage = new MaterialUsage();
            usage.setProject(p);
            usage.setMaterial(m);
            usage.setQuantityUsed(dto.getQuantityUsed());
            usage.setUnitPrice(stock.getAverageUnitPrice()); // Cost based on avg stock price
            usage.setTotalCost(dto.getQuantityUsed().multiply(stock.getAverageUnitPrice()));
            usage.setUsageDate(dto.getUsageDate());
            usage.setActivityDescription(dto.getActivityDescription());
            usage.setRecordedBy(u);
            usage = dao.saveWithSession(usage, session);

            // 2. Update Stock
            BigDecimal newQty = stock.getQuantityAvailable().subtract(dto.getQuantityUsed());
            stock.setQuantityAvailable(newQty);
            stockDao.updateWithSession(stock, session);

            // 3. Create Movement Record
            MaterialStockMovement movement = new MaterialStockMovement();
            movement.setProject(p);
            movement.setMaterial(m);
            movement.setMovementType(EMovementType.OUT);
            movement.setQuantity(dto.getQuantityUsed());
            movement.setUnitPrice(stock.getAverageUnitPrice());
            movement.setTotalPrice(usage.getTotalCost());
            movement.setMovementDate(dto.getUsageDate());
            movement.setDescription("Used in activity: " + dto.getActivityDescription());
            movement.setReferenceType("USAGE");
            movement.setReferenceId(usage.getId());
            movement.setRecordedBy(u);
            movementDao.saveWithSession(movement, session);

            tx.commit();
            return toDTO(usage);
        } catch (IllegalArgumentException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to record atomic material usage");
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public MaterialUsageDTO updateUsage(MaterialUsageDTO dto) throws RemoteException {
        throw new RemoteException("Updating material usage is not supported to maintain stock audit trail.");
    }

    @Override
    public boolean deleteUsage(String usageId) throws RemoteException {
        throw new RemoteException("Deleting material usage is not supported to maintain stock audit trail.");
    }

    @Override
    public MaterialUsageDTO getUsageById(String usageId) throws RemoteException {
        try {
            return toDTO(dao.findById(usageId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch usage");
        }
    }

    @Override
    public List<MaterialUsageDTO> getUsageByProject(String projectId) throws RemoteException {
        try {
            List<MaterialUsageDTO> list = new ArrayList<>();
            for (MaterialUsage mu : dao.findByProject(projectId)) {
                list.add(toDTO(mu));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch usage by project");
        }
    }

    @Override
    public List<MaterialUsageDTO> getUsageByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        try {
            List<MaterialUsageDTO> list = new ArrayList<>();
            for (MaterialUsage mu : dao.findByProjectAndDateRange(projectId, from, to)) {
                list.add(toDTO(mu));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch usage by date range");
        }
    }
}
