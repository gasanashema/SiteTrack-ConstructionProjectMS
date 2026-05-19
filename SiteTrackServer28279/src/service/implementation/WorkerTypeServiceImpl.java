package service.implementation;

import dao.WorkerTypeDao;
import dto.WorkerTypeDTO;
import model.WorkerType;
import service.interfaces.WorkerTypeService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class WorkerTypeServiceImpl extends UnicastRemoteObject implements WorkerTypeService {
    private final WorkerTypeDao dao;

    public WorkerTypeServiceImpl() throws RemoteException {
        super();
        this.dao = new WorkerTypeDao();
    }

    private WorkerTypeDTO toDTO(WorkerType entity) {
        if (entity == null) return null;
        return new WorkerTypeDTO(entity.getId(), entity.getTypeName(), entity.getDefaultDailyRate(), entity.getDescription());
    }

    private WorkerType toEntity(WorkerTypeDTO dto) {
        if (dto == null) return null;
        WorkerType entity = new WorkerType();
        entity.setId(dto.getId());
        entity.setTypeName(dto.getTypeName());
        entity.setDefaultDailyRate(dto.getDefaultDailyRate());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public WorkerTypeDTO createWorkerType(WorkerTypeDTO dto) throws RemoteException {
        try {
            WorkerType entity = toEntity(dto);
            entity.setCreatedAt(java.time.LocalDateTime.now());
            entity.setUpdatedAt(java.time.LocalDateTime.now());
            entity = dao.save(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create worker type");
        }
    }

    @Override
    public WorkerTypeDTO updateWorkerType(WorkerTypeDTO dto) throws RemoteException {
        try {
            WorkerType existing = dao.findById(dto.getId());
            if (existing != null) {
                existing.setTypeName(dto.getTypeName());
                existing.setDefaultDailyRate(dto.getDefaultDailyRate());
                existing.setDescription(dto.getDescription());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                existing = dao.update(existing);
                return toDTO(existing);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update worker type");
        }
    }

    @Override
    public boolean deleteWorkerType(String workerTypeId) throws RemoteException {
        try {
            return dao.delete(workerTypeId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete worker type");
        }
    }

    @Override
    public WorkerTypeDTO getWorkerTypeById(String workerTypeId) throws RemoteException {
        try {
            return toDTO(dao.findById(workerTypeId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch worker type");
        }
    }

    @Override
    public List<WorkerTypeDTO> getAllWorkerTypes() throws RemoteException {
        try {
            List<WorkerTypeDTO> dtoList = new ArrayList<>();
            for (WorkerType entity : dao.findAll()) {
                dtoList.add(toDTO(entity));
            }
            return dtoList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch worker types");
        }
    }

    @Override
    public boolean workerTypeNameExists(String typeName) throws RemoteException {
        try {
            for(WorkerType wt : dao.findAll()) {
                if(wt.getTypeName().equalsIgnoreCase(typeName)) return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check worker type existence");
        }
    }
}
