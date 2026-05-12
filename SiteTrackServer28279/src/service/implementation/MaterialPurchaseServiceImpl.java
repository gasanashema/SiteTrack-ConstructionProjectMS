package service.implementation;

import dao.MaterialPurchaseDao;
import dao.ProjectMaterialStockDao;
import dao.MaterialStockMovementDao;
import dao.ProjectDao;
import dao.MaterialDao;
import dao.UserDao;
import dto.MaterialPurchaseDTO;
import model.MaterialPurchase;
import model.ProjectMaterialStock;
import model.MaterialStockMovement;
import model.Project;
import model.Material;
import model.User;
import model.EMovementType;
import service.interfaces.MaterialPurchaseService;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MaterialPurchaseServiceImpl extends UnicastRemoteObject implements MaterialPurchaseService {
    private final MaterialPurchaseDao dao;
    private final ProjectMaterialStockDao stockDao;
    private final MaterialStockMovementDao movementDao;
    private final ProjectDao projectDao;
    private final MaterialDao materialDao;
    private final UserDao userDao;

    public MaterialPurchaseServiceImpl() throws RemoteException {
        super();
        this.dao = new MaterialPurchaseDao();
        this.stockDao = new ProjectMaterialStockDao();
        this.movementDao = new MaterialStockMovementDao();
        this.projectDao = new ProjectDao();
        this.materialDao = new MaterialDao();
        this.userDao = new UserDao();
    }

    private MaterialPurchaseDTO toDTO(MaterialPurchase entity) {
        if (entity == null) return null;
        return new MaterialPurchaseDTO(
            entity.getId(),
            entity.getProject() != null ? entity.getProject().getId() : null,
            entity.getProject() != null ? entity.getProject().getProjectName() : null,
            entity.getMaterial() != null ? entity.getMaterial().getId() : null,
            entity.getMaterial() != null ? entity.getMaterial().getMaterialName() : null,
            entity.getMaterial() != null ? entity.getMaterial().getUnit() : null,
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getTotalPrice(),
            entity.getSupplierName(),
            entity.getPurchaseDate(),
            entity.getRecordedBy() != null ? entity.getRecordedBy().getFullName() : null
        );
    }

    @Override
    public MaterialPurchaseDTO recordPurchase(MaterialPurchaseDTO dto) throws RemoteException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Project p = projectDao.findById(dto.getProjectId());
            Material m = materialDao.findById(dto.getMaterialId());
            User u = userDao.findById(dto.getRecordedByName()); // Assuming username/id

            if (p == null || m == null || u == null) throw new IllegalArgumentException("Project, Material, or User not found");

            // 1. Create Purchase Record
            MaterialPurchase purchase = new MaterialPurchase();
            purchase.setProject(p);
            purchase.setMaterial(m);
            purchase.setQuantity(dto.getQuantity());
            purchase.setUnitPrice(dto.getUnitPrice());
            purchase.setTotalPrice(dto.getTotalPrice());
            purchase.setSupplierName(dto.getSupplierName());
            purchase.setPurchaseDate(dto.getPurchaseDate());
            purchase.setRecordedBy(u);
            purchase = dao.saveWithSession(purchase, session);

            // 2. Update or Create Stock
            ProjectMaterialStock stock = stockDao.findByProjectAndMaterial(p.getId(), m.getId());
            if (stock == null) {
                stock = new ProjectMaterialStock();
                stock.setProject(p);
                stock.setMaterial(m);
                stock.setQuantityAvailable(dto.getQuantity());
                stock.setMinimumQuantity(BigDecimal.ZERO); // default
                stock.setAverageUnitPrice(dto.getUnitPrice());
                stockDao.saveWithSession(stock, session);
            } else {
                BigDecimal oldTotalVal = stock.getQuantityAvailable().multiply(stock.getAverageUnitPrice());
                BigDecimal newTotalVal = dto.getQuantity().multiply(dto.getUnitPrice());
                BigDecimal newQty = stock.getQuantityAvailable().add(dto.getQuantity());
                
                stock.setQuantityAvailable(newQty);
                stock.setAverageUnitPrice((oldTotalVal.add(newTotalVal)).divide(newQty, 2, java.math.RoundingMode.HALF_UP));
                stockDao.updateWithSession(stock, session);
            }

            // 3. Create Movement Record
            MaterialStockMovement movement = new MaterialStockMovement();
            movement.setProject(p);
            movement.setMaterial(m);
            movement.setMovementType(EMovementType.IN);
            movement.setQuantity(dto.getQuantity());
            movement.setUnitPrice(dto.getUnitPrice());
            movement.setTotalPrice(dto.getTotalPrice());
            movement.setMovementDate(dto.getPurchaseDate());
            movement.setDescription("Purchase from " + dto.getSupplierName());
            movement.setReferenceType("PURCHASE");
            movement.setReferenceId(purchase.getId());
            movement.setRecordedBy(u);
            movementDao.saveWithSession(movement, session);

            tx.commit();
            return toDTO(purchase);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed to record atomic material purchase");
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public MaterialPurchaseDTO updatePurchase(MaterialPurchaseDTO dto) throws RemoteException {
        // Full atomic update of a purchase is highly complex (re-calculating stock backwards).
        // Usually, in audit systems, purchases shouldn't be directly modified. 
        // We will throw UnsupportedOperationException or implement a very basic update without stock sync.
        throw new RemoteException("Updating material purchases is not supported to maintain stock audit trail. Please use stock adjustments.");
    }

    @Override
    public boolean deletePurchase(String purchaseId) throws RemoteException {
        throw new RemoteException("Deleting material purchases is not supported to maintain stock audit trail.");
    }

    @Override
    public MaterialPurchaseDTO getPurchaseById(String purchaseId) throws RemoteException {
        try {
            return toDTO(dao.findById(purchaseId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch purchase");
        }
    }

    @Override
    public List<MaterialPurchaseDTO> getPurchasesByProject(String projectId) throws RemoteException {
        try {
            List<MaterialPurchaseDTO> list = new ArrayList<>();
            for (MaterialPurchase mp : dao.findByProject(projectId)) {
                list.add(toDTO(mp));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch purchases");
        }
    }

    @Override
    public List<MaterialPurchaseDTO> getPurchasesByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        try {
            List<MaterialPurchaseDTO> list = new ArrayList<>();
            for (MaterialPurchase mp : dao.findByProjectAndDateRange(projectId, from, to)) {
                list.add(toDTO(mp));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch purchases by date range");
        }
    }
}
