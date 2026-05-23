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
    private final dao.WorkerAssignmentDao assignmentDao;
    private final dao.ProjectDao projectDao;

    public SiteWorkerServiceImpl() throws RemoteException {
        super();
        this.dao = new SiteWorkerDao();
        this.workerTypeDao = new WorkerTypeDao();
        this.assignmentDao = new dao.WorkerAssignmentDao();
        this.projectDao = new dao.ProjectDao();
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
            entity.setCreatedAt(java.time.LocalDateTime.now());
            entity.setUpdatedAt(java.time.LocalDateTime.now());
            
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
            entity.setUpdatedAt(java.time.LocalDateTime.now());
            
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
                entity.setUpdatedAt(java.time.LocalDateTime.now());
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
                entity.setUpdatedAt(java.time.LocalDateTime.now());
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
    public boolean deleteWorker(String workerId) throws RemoteException {
        try {
            dao.delete(workerId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete worker");
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
            List<SiteWorkerDTO> list = new ArrayList<>();
            List<model.WorkerAssignment> assignments = assignmentDao.findByProject(projectId, model.EAssignmentStatus.ACTIVE);
            for (model.WorkerAssignment a : assignments) {
                if (a.getWorker() != null) {
                    list.add(toDTO(a.getWorker()));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch workers by project");
        }
    }

    @Override
    public boolean assignWorkers(List<String> workerIds, String projectId, java.time.LocalDate date) throws RemoteException {
        try {
            model.Project project = projectDao.findById(projectId);
            if (project == null) throw new IllegalArgumentException("Project not found");

            for (String wId : workerIds) {
                // Check if already active
                model.WorkerAssignment active = assignmentDao.findActiveAssignmentByWorker(wId);
                if (active != null) {
                    continue; // Skip if already active somewhere
                }
                SiteWorker worker = dao.findById(wId);
                if (worker != null) {
                    model.WorkerAssignment wa = new model.WorkerAssignment();
                    wa.setWorker(worker);
                    wa.setProject(project);
                    wa.setStatus(model.EAssignmentStatus.ACTIVE);
                    wa.setAssignedDate(date);
                    wa.setCreatedAt(java.time.LocalDateTime.now());
                    wa.setUpdatedAt(java.time.LocalDateTime.now());
                    assignmentDao.save(wa);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to assign workers");
        }
    }

    @Override
    public boolean transferWorker(String workerId, String toProjectId, java.time.LocalDate date) throws RemoteException {
        try {
            model.Project toProject = projectDao.findById(toProjectId);
            if (toProject == null) throw new IllegalArgumentException("Destination project not found");

            model.WorkerAssignment active = assignmentDao.findActiveAssignmentByWorker(workerId);
            if (active != null) {
                active.setStatus(model.EAssignmentStatus.TRANSFERRED);
                active.setEndDate(date);
                active.setUpdatedAt(java.time.LocalDateTime.now());
                assignmentDao.update(active);
            }

            SiteWorker worker = dao.findById(workerId);
            if (worker != null) {
                model.WorkerAssignment wa = new model.WorkerAssignment();
                wa.setWorker(worker);
                wa.setProject(toProject);
                wa.setStatus(model.EAssignmentStatus.ACTIVE);
                wa.setAssignedDate(date);
                wa.setCreatedAt(java.time.LocalDateTime.now());
                wa.setUpdatedAt(java.time.LocalDateTime.now());
                assignmentDao.save(wa);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to transfer worker");
        }
    }

    @Override
    public List<dto.WorkerAssignmentDTO> getActiveAssignments() throws RemoteException {
        try {
            List<dto.WorkerAssignmentDTO> dtos = new ArrayList<>();
            // Temporary simple fetch to all active assignments by going through all projects.
            // But better to just run a native query in DAO. Since we don't have a direct findAllActiveAssignments yet,
            // I'll add a helper method in DAO if needed. For now I will just grab all workers and check.
            List<SiteWorker> workers = dao.findAllActive();
            for (SiteWorker w : workers) {
                model.WorkerAssignment a = assignmentDao.findActiveAssignmentByWorker(w.getId());
                if (a != null) {
                    dtos.add(new dto.WorkerAssignmentDTO(
                        a.getId(), a.getWorker().getId(), a.getWorker().getFullName(),
                        a.getWorker().getWorkerType() != null ? a.getWorker().getWorkerType().getTypeName() : null,
                        a.getProject().getId(), a.getProject().getProjectName(),
                        a.getStatus().name(), a.getAssignedDate(), a.getEndDate()
                    ));
                }
            }
            return dtos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get active assignments");
        }
    }
}
