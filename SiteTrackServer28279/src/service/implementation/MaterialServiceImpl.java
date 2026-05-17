package service.implementation;

import dao.MaterialDao;
import dao.MaterialCategoryDao;
import dto.MaterialDTO;
import model.Material;
import model.MaterialCategory;
import model.EMaterialStatus;
import service.interfaces.MaterialService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MaterialServiceImpl extends UnicastRemoteObject implements MaterialService {
    private final MaterialDao dao;
    private final MaterialCategoryDao categoryDao;

    public MaterialServiceImpl() throws RemoteException {
        super();
        this.dao = new MaterialDao();
        this.categoryDao = new MaterialCategoryDao();
    }

    private MaterialDTO toDTO(Material entity) {
        if (entity == null) return null;
        return new MaterialDTO(
            entity.getId(),
            entity.getCategory() != null ? entity.getCategory().getId() : null,
            entity.getCategory() != null ? entity.getCategory().getCategoryName() : null,
            entity.getMaterialName(),
            entity.getUnit(),
            entity.getDescription(),
            entity.getStatus().name()
        );
    }

    @Override
    public MaterialDTO createMaterial(MaterialDTO dto) throws RemoteException {
        try {
            Material entity = new Material();
            entity.setMaterialName(dto.getMaterialName());
            entity.setUnit(dto.getUnit());
            entity.setDescription(dto.getDescription());
            entity.setStatus(EMaterialStatus.ACTIVE);
            
            if (dto.getCategoryId() != null) {
                MaterialCategory cat = categoryDao.findById(dto.getCategoryId());
                if (cat != null) entity.setCategory(cat);
            }
            
            entity.setCreatedAt(java.time.LocalDateTime.now());
            entity.setUpdatedAt(java.time.LocalDateTime.now());
            
            entity = dao.save(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create material");
        }
    }

    @Override
    public MaterialDTO updateMaterial(MaterialDTO dto) throws RemoteException {
        try {
            Material entity = dao.findById(dto.getId());
            if (entity == null) throw new IllegalArgumentException("Material not found");
            
            entity.setMaterialName(dto.getMaterialName());
            entity.setUnit(dto.getUnit());
            entity.setDescription(dto.getDescription());
            
            if (dto.getCategoryId() != null) {
                MaterialCategory cat = categoryDao.findById(dto.getCategoryId());
                if (cat != null) entity.setCategory(cat);
            }
            
            entity = dao.update(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update material");
        }
    }

    @Override
    public boolean deactivateMaterial(String materialId) throws RemoteException {
        try {
            Material entity = dao.findById(materialId);
            if (entity != null) {
                entity.setStatus(EMaterialStatus.INACTIVE);
                dao.update(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to deactivate material");
        }
    }

    @Override
    public boolean activateMaterial(String materialId) throws RemoteException {
        try {
            Material entity = dao.findById(materialId);
            if (entity != null) {
                entity.setStatus(EMaterialStatus.ACTIVE);
                dao.update(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to activate material");
        }
    }

    @Override
    public boolean deleteMaterial(String materialId) throws RemoteException {
        try {
            return dao.delete(materialId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete material");
        }
    }

    @Override
    public MaterialDTO getMaterialById(String materialId) throws RemoteException {
        try {
            return toDTO(dao.findById(materialId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch material");
        }
    }

    @Override
    public List<MaterialDTO> getAllMaterials() throws RemoteException {
        try {
            List<MaterialDTO> list = new ArrayList<>();
            for (Material m : dao.findAll()) {
                list.add(toDTO(m));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch materials");
        }
    }

    @Override
    public List<MaterialDTO> getActiveMaterials() throws RemoteException {
        try {
            List<MaterialDTO> list = new ArrayList<>();
            for (Material m : dao.findAllActive()) {
                list.add(toDTO(m));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch active materials");
        }
    }

    @Override
    public List<MaterialDTO> getMaterialsByCategory(String categoryId) throws RemoteException {
        try {
            List<MaterialDTO> list = new ArrayList<>();
            for (Material m : dao.findByCategory(categoryId)) {
                list.add(toDTO(m));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch materials by category");
        }
    }
}
