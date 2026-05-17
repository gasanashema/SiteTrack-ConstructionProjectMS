package service.implementation;

import dao.MaterialCategoryDao;
import dto.MaterialCategoryDTO;
import model.MaterialCategory;
import service.interfaces.MaterialCategoryService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MaterialCategoryServiceImpl extends UnicastRemoteObject implements MaterialCategoryService {
    private final MaterialCategoryDao dao;

    public MaterialCategoryServiceImpl() throws RemoteException {
        super();
        this.dao = new MaterialCategoryDao();
    }

    private MaterialCategoryDTO toDTO(MaterialCategory entity) {
        if (entity == null) return null;
        return new MaterialCategoryDTO(entity.getId(), entity.getCategoryName(), entity.getDescription(), entity.getUnit());
    }

    private MaterialCategory toEntity(MaterialCategoryDTO dto) {
        if (dto == null) return null;
        MaterialCategory entity = new MaterialCategory();
        entity.setId(dto.getId());
        entity.setCategoryName(dto.getCategoryName());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        return entity;
    }

    @Override
    public MaterialCategoryDTO createCategory(MaterialCategoryDTO dto) throws RemoteException {
        try {
            MaterialCategory entity = toEntity(dto);
            entity.setCreatedAt(java.time.LocalDateTime.now());
            entity.setUpdatedAt(java.time.LocalDateTime.now());
            entity = dao.save(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create category: " + e.getMessage());
        }
    }

    @Override
    public MaterialCategoryDTO updateCategory(MaterialCategoryDTO dto) throws RemoteException {
        try {
            MaterialCategory entity = toEntity(dto);
            entity = dao.update(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update category");
        }
    }

    @Override
    public boolean deleteCategory(String categoryId) throws RemoteException {
        try {
            return dao.delete(categoryId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete category");
        }
    }

    @Override
    public MaterialCategoryDTO getCategoryById(String categoryId) throws RemoteException {
        try {
            return toDTO(dao.findById(categoryId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch category");
        }
    }

    @Override
    public List<MaterialCategoryDTO> getAllCategories() throws RemoteException {
        try {
            List<MaterialCategoryDTO> dtoList = new ArrayList<>();
            for (MaterialCategory entity : dao.findAll()) {
                dtoList.add(toDTO(entity));
            }
            return dtoList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch categories");
        }
    }

    @Override
    public boolean categoryNameExists(String name) throws RemoteException {
        try {
            return dao.existsByName(name); // Assuming existsByName is in Dao, or we can check manually
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check category existence");
        }
    }
}
