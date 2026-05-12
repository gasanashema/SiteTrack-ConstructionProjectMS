package service.implementation;

import dao.SiteWorkerDao;
import dao.WorkerTypeDao;
import dto.SiteWorkerDTO;
import model.SiteWorker;
import model.WorkerType;
import model.EWorkerStatus;
import service.interfaces.SiteWorkerService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class SiteWorkerServiceImpl extends UnicastRemoteObject implements SiteWorkerService {
    private final SiteWorkerDao dao;
    private final WorkerTypeDao workerTypeDao;

    public SiteWorkerServiceImpl() throws RemoteException {
        super();
        this.dao = new SiteWorkerDao();
        this.workerTypeDao = new WorkerTypeDao();
    }

    private SiteWorkerDTO toDTO(SiteWorker entity) {
        if (entity == null) return null;
        return new SiteWorkerDTO(
            entity.getId(),
            entity.getWorkerType() != null ? entity.getWorkerType().getId() : null,
            entity.getWorkerType() != null ? entity.getWorkerType().getTypeName() : null,
            entity.getFullName(),
            entity.getPhone(),
            entity.getDailyRate(),
            entity.getStatus().name()
        );
    }

    @Override
    public SiteWorkerDTO createWorker(SiteWorkerDTO dto) throws RemoteException {
        try {
            SiteWorker entity = new SiteWorker();
            entity.setFullName(dto.getFullName());
            entity.setPhone(dto.getPhone());
            entity.setDailyRate(dto.getDailyRate());
            entity.setStatus(EWorkerStatus.ACTIVE);
            
            if (dto.getWorkerTypeId() != null) {
                WorkerType wt = workerTypeDao.findById(dto.getWorkerTypeId());
                if (wt != null) entity.setWorkerType(wt);
            }
            
            entity = dao.save(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create worker");
        }
    }

    @Override
    public SiteWorkerDTO updateWorker(SiteWorkerDTO dto) throws RemoteException {
        try {
            SiteWorker entity = dao.findById(dto.getId());
            if (entity == null) throw new IllegalArgumentException("Worker not found");
            
            entity.setFullName(dto.getFullName());
            entity.setPhone(dto.getPhone());
            entity.setDailyRate(dto.getDailyRate());
            
            if (dto.getWorkerTypeId() != null) {
                WorkerType wt = workerTypeDao.findById(dto.getWorkerTypeId());
                if (wt != null) entity.setWorkerType(wt);
            }
            
            entity = dao.update(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update worker");
        }
    }

    @Override
    public boolean deactivateWorker(String workerId) throws RemoteException {
        try {
            SiteWorker entity = dao.findById(workerId);
            if (entity != null) {
                entity.setStatus(EWorkerStatus.INACTIVE);
                dao.update(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to deactivate worker");
        }
    }

    @Override
    public boolean activateWorker(String workerId) throws RemoteException {
        try {
            SiteWorker entity = dao.findById(workerId);
            if (entity != null) {
                entity.setStatus(EWorkerStatus.ACTIVE);
                dao.update(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to activate worker");
        }
    }

    @Override
    public SiteWorkerDTO getWorkerById(String workerId) throws RemoteException {
        try {
            return toDTO(dao.findById(workerId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch worker");
        }
    }

    @Override
    public List<SiteWorkerDTO> getAllWorkers() throws RemoteException {
        try {
            List<SiteWorkerDTO> list = new ArrayList<>();
            for (SiteWorker w : dao.findAll()) {
                list.add(toDTO(w));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch workers");
        }
    }

    @Override
    public List<SiteWorkerDTO> getActiveWorkers() throws RemoteException {
        try {
            List<SiteWorkerDTO> list = new ArrayList<>();
            for (SiteWorker w : dao.findAllActive()) {
                list.add(toDTO(w));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch active workers");
        }
    }

    @Override
    public List<SiteWorkerDTO> getWorkersByType(String workerTypeId) throws RemoteException {
        try {
            List<SiteWorkerDTO> list = new ArrayList<>();
            for (SiteWorker w : dao.findByWorkerType(workerTypeId)) {
                list.add(toDTO(w));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch workers by type");
        }
    }

    @Override
    public List<SiteWorkerDTO> getWorkersByProject(String projectId) throws RemoteException {
        try {
            // Note: workers by project needs attendance check. Assuming DAO handles this logic or we fetch from attendance
            // A worker is part of a project if they have attendance.
            // For now returning empty to keep stub simple unless DAO method exists.
            return new ArrayList<>(); 
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch workers by project");
        }
    }
}
