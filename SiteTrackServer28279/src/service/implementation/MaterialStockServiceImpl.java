package service.implementation;

import dao.ProjectMaterialStockDao;
import dao.MaterialStockMovementDao;
import dao.ProjectDao;
import dao.MaterialDao;
import dao.UserDao;
import dto.ProjectMaterialStockDTO;
import dto.MaterialStockMovementDTO;
import model.ProjectMaterialStock;
import model.MaterialStockMovement;
import model.Project;
import model.Material;
import model.User;
import model.EMovementType;
import service.interfaces.MaterialStockService;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MaterialStockServiceImpl extends UnicastRemoteObject implements MaterialStockService {
    private final ProjectMaterialStockDao dao;
    private final MaterialStockMovementDao movementDao;
    private final ProjectDao projectDao;
    private final MaterialDao materialDao;
    private final UserDao userDao;

    public MaterialStockServiceImpl() throws RemoteException {
        super();
        this.dao = new ProjectMaterialStockDao();
        this.movementDao = new MaterialStockMovementDao();
        this.projectDao = new ProjectDao();
        this.materialDao = new MaterialDao();
        this.userDao = new UserDao();
    }

    private ProjectMaterialStockDTO toDTO(ProjectMaterialStock entity) {
        if (entity == null) return null;
        boolean belowMin = entity.getQuantityAvailable().compareTo(entity.getMinimumQuantity()) < 0;
        return new ProjectMaterialStockDTO(
            entity.getId(),
            entity.getProject() != null ? entity.getProject().getId() : null,
            entity.getProject() != null ? entity.getProject().getProjectName() : null,
            entity.getMaterial() != null ? entity.getMaterial().getId() : null,
            entity.getMaterial() != null ? entity.getMaterial().getMaterialName() : null,
            entity.getMaterial() != null ? entity.getMaterial().getUnit() : null,
            entity.getQuantityAvailable(),
            entity.getMinimumQuantity(),
            entity.getAverageUnitPrice(),
            belowMin
        );
    }

    private MaterialStockMovementDTO toDTO(MaterialStockMovement entity) {
        if (entity == null) return null;
        return new MaterialStockMovementDTO(
            entity.getId(),
            entity.getProject() != null ? entity.getProject().getId() : null,
            entity.getProject() != null ? entity.getProject().getProjectName() : null,
            entity.getMaterial() != null ? entity.getMaterial().getId() : null,
            entity.getMaterial() != null ? entity.getMaterial().getMaterialName() : null,
            entity.getMovementType().name(),
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getTotalPrice(),
            entity.getMovementDate(),
            entity.getDescription(),
            entity.getReferenceType(),
            entity.getReferenceId(),
            entity.getRecordedBy() != null ? entity.getRecordedBy().getFullName() : null
        );
    }

    @Override
    public List<ProjectMaterialStockDTO> getStockByProject(String projectId) throws RemoteException {
        try {
            List<ProjectMaterialStockDTO> list = new ArrayList<>();
            for (ProjectMaterialStock s : dao.findByProject(projectId)) {
                list.add(toDTO(s));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch stock");
        }
    }

    @Override
    public ProjectMaterialStockDTO getStockByProjectAndMaterial(String projectId, String materialId) throws RemoteException {
        try {
            return toDTO(dao.findByProjectAndMaterial(projectId, materialId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch specific stock");
        }
    }

    @Override
    public List<ProjectMaterialStockDTO> getLowStockByProject(String projectId) throws RemoteException {
        try {
            List<ProjectMaterialStockDTO> list = new ArrayList<>();
            for (ProjectMaterialStock s : dao.findBelowMinimum(projectId)) {
                list.add(toDTO(s));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch low stock");
        }
    }

    @Override
    public boolean updateMinimumQuantity(String stockId, BigDecimal minimumQty) throws RemoteException {
        try {
            ProjectMaterialStock s = dao.findById(stockId);
            if (s != null) {
                s.setMinimumQuantity(minimumQty);
                dao.update(s);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update minimum quantity");
        }
    }

    @Override
    public boolean recordStockAdjustment(String projectId, String materialId, BigDecimal newQuantity, String description, String recordedById) throws RemoteException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Project p = projectDao.findById(projectId);
            Material m = materialDao.findById(materialId);
            User u = userDao.findById(recordedById); 

            if (p == null || m == null || u == null) throw new IllegalArgumentException("Project, Material, or User not found");

            ProjectMaterialStock stock = dao.findByProjectAndMaterial(p.getId(), m.getId());
            if (stock == null) throw new IllegalArgumentException("Stock record does not exist to adjust");

            BigDecimal diff = newQuantity.subtract(stock.getQuantityAvailable());
            if (diff.compareTo(BigDecimal.ZERO) == 0) return true; // No change

            // 1. Update Stock
            stock.setQuantityAvailable(newQuantity);
            dao.updateWithSession(stock, session);

            // 2. Create Movement Record
            MaterialStockMovement movement = new MaterialStockMovement();
            movement.setProject(p);
            movement.setMaterial(m);
            movement.setMovementType(EMovementType.ADJUSTMENT);
            movement.setQuantity(diff.abs());
            movement.setUnitPrice(stock.getAverageUnitPrice());
            movement.setTotalPrice(diff.abs().multiply(stock.getAverageUnitPrice()));
            movement.setMovementDate(LocalDate.now());
            movement.setDescription(description);
            movement.setReferenceType("MANUAL_ADJUSTMENT");
            movement.setReferenceId("N/A");
            movement.setRecordedBy(u);
            movementDao.saveWithSession(movement, session);

            tx.commit();
            return true;
        } catch (IllegalArgumentException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to record atomic stock adjustment");
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<MaterialStockMovementDTO> getMovementsByProject(String projectId) throws RemoteException {
        try {
            List<MaterialStockMovementDTO> list = new ArrayList<>();
            for (MaterialStockMovement sm : movementDao.findByProject(projectId)) {
                list.add(toDTO(sm));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch movements");
        }
    }

    @Override
    public List<MaterialStockMovementDTO> getMovementsByProjectAndMaterial(String projectId, String materialId) throws RemoteException {
        try {
            List<MaterialStockMovementDTO> list = new ArrayList<>();
            for (MaterialStockMovement sm : movementDao.findByProjectAndMaterial(projectId, materialId)) {
                list.add(toDTO(sm));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch specific movements");
        }
    }

    @Override
    public List<MaterialStockMovementDTO> getMovementsByDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        try {
            List<MaterialStockMovementDTO> list = new ArrayList<>();
            for (MaterialStockMovement sm : movementDao.findByProjectAndDateRange(projectId, from, to)) {
                list.add(toDTO(sm));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch movements by date range");
        }
    }
}
