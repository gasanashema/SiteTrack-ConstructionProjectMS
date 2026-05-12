package service.interfaces;

import dto.MaterialCategoryDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MaterialCategoryService extends Remote {
    MaterialCategoryDTO createCategory(MaterialCategoryDTO dto) throws RemoteException;
    MaterialCategoryDTO updateCategory(MaterialCategoryDTO dto) throws RemoteException;
    boolean deleteCategory(String categoryId) throws RemoteException;
    MaterialCategoryDTO getCategoryById(String categoryId) throws RemoteException;
    List<MaterialCategoryDTO> getAllCategories() throws RemoteException;
    boolean categoryNameExists(String name) throws RemoteException;
}
