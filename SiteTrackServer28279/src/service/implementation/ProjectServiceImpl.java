package service.implementation;

import dao.ProjectDao;
import dao.UserDao;
import dao.ProjectManagerDao;
import dto.ProjectDTO;
import dto.ProjectManagerDTO;
import dto.ProjectSummaryDTO;
import model.Project;
import model.ProjectManager;
import model.User;
import model.EProjectStatus;
import model.EManagerStatus;
import service.interfaces.ProjectService;
import util.NotificationProducer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImpl extends UnicastRemoteObject implements ProjectService {
    private final ProjectDao dao;
    private final UserDao userDao;
    private final ProjectManagerDao pmDao;

    public ProjectServiceImpl() throws RemoteException {
        super();
        this.dao = new ProjectDao();
        this.userDao = new UserDao();
        this.pmDao = new ProjectManagerDao();
    }

    private ProjectDTO toDTO(Project entity) {
        if (entity == null) return null;
        return new ProjectDTO(
            entity.getId(),
            entity.getProjectName(),
            entity.getLocation(),
            entity.getDescription(),
            entity.getStartDate(),
            entity.getExpectedEndDate(),
            entity.getStatus().name(),
            entity.getCreatedBy() != null ? entity.getCreatedBy().getFullName() : null,
            entity.getCreatedAt()
        );
    }

    @Override
    public ProjectDTO createProject(ProjectDTO dto) throws RemoteException {
        try {
            Project entity = new Project();
            entity.setProjectName(dto.getProjectName());
            entity.setLocation(dto.getLocation());
            entity.setDescription(dto.getDescription());
            entity.setStartDate(dto.getStartDate());
            entity.setExpectedEndDate(dto.getExpectedEndDate());
            entity.setStatus(EProjectStatus.PLANNING);
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            
            // Assuming we pass userId mapping via createdByName for creation stub
            User u = userDao.findById(dto.getCreatedByName()); 
            if (u != null) entity.setCreatedBy(u);
            
            entity = dao.save(entity);
            NotificationProducer.sendNotification("ADMIN", "PROJECT_CREATED", "New project created: " + entity.getProjectName(), "SYSTEM");
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create project");
        }
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO dto) throws RemoteException {
        try {
            Project entity = dao.findById(dto.getId());
            if (entity == null) throw new IllegalArgumentException("Project not found");
            
            entity.setProjectName(dto.getProjectName());
            entity.setLocation(dto.getLocation());
            entity.setDescription(dto.getDescription());
            entity.setStartDate(dto.getStartDate());
            entity.setExpectedEndDate(dto.getExpectedEndDate());
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                entity.setStatus(EProjectStatus.valueOf(dto.getStatus()));
            }
            entity.setUpdatedAt(LocalDateTime.now());
            
            entity = dao.update(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update project");
        }
    }

    @Override
    public boolean deleteProject(String projectId) throws RemoteException {
        try {
            return dao.delete(projectId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete project");
        }
    }

    @Override
    public boolean changeProjectStatus(String projectId, String status) throws RemoteException {
        try {
            Project entity = dao.findById(projectId);
            if (entity != null) {
                entity.setStatus(EProjectStatus.valueOf(status));
                entity.setUpdatedAt(LocalDateTime.now());
                dao.update(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to change project status");
        }
    }

    @Override
    public ProjectDTO getProjectById(String projectId) throws RemoteException {
        try {
            return toDTO(dao.findById(projectId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch project");
        }
    }

    @Override
    public List<ProjectDTO> getAllProjects() throws RemoteException {
        try {
            List<ProjectDTO> list = new ArrayList<>();
            for (Project p : dao.findAll()) {
                list.add(toDTO(p));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch projects");
        }
    }

    @Override
    public List<ProjectDTO> getProjectsByStatus(String status) throws RemoteException {
        try {
            List<ProjectDTO> list = new ArrayList<>();
            for (Project p : dao.findByStatus(EProjectStatus.valueOf(status))) {
                list.add(toDTO(p));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch projects by status");
        }
    }

    @Override
    public List<ProjectDTO> getProjectsManagedBy(String userId) throws RemoteException {
        try {
            List<ProjectDTO> list = new ArrayList<>();
            for (ProjectManager pm : pmDao.findByUser(userId)) {
                if (pm.getProject() != null && pm.getStatus() == EManagerStatus.ACTIVE) {
                    list.add(toDTO(pm.getProject()));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch managed projects");
        }
    }

    @Override
    public boolean assignManager(String projectId, String userId) throws RemoteException {
        try {
            Project p = dao.findById(projectId);
            User u = userDao.findById(userId);
            if (p != null && u != null) {
                ProjectManager pm = new ProjectManager();
                pm.setProject(p);
                pm.setUser(u);
                pm.setAssignedDate(LocalDate.now());
                pm.setStatus(EManagerStatus.ACTIVE);
                pm.setCreatedAt(LocalDateTime.now());
                pm.setUpdatedAt(LocalDateTime.now());
                pmDao.save(pm);
                NotificationProducer.sendNotification(userId, "MANAGER_ASSIGNED", "Assigned as manager for: " + p.getProjectName(), "EMAIL");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to assign manager: " + e.getMessage());
        }
    }

    @Override
    public boolean removeManager(String projectId, String userId) throws RemoteException {
        try {
            ProjectManager pm = pmDao.findByProjectAndUser(projectId, userId);
            if (pm != null) {
                pm.setStatus(EManagerStatus.REMOVED);
                pmDao.update(pm);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to remove manager");
        }
    }

    @Override
    public List<ProjectManagerDTO> getManagersByProject(String projectId) throws RemoteException {
        try {
            List<ProjectManagerDTO> list = new ArrayList<>();
            for (ProjectManager pm : pmDao.findByProject(projectId)) {
                list.add(new ProjectManagerDTO(
                    pm.getId(),
                    pm.getProject() != null ? pm.getProject().getId() : null,
                    pm.getProject() != null ? pm.getProject().getProjectName() : null,
                    pm.getUser() != null ? pm.getUser().getId() : null,
                    pm.getUser() != null ? pm.getUser().getFullName() : null,
                    pm.getAssignedDate(),
                    pm.getStatus() != null ? pm.getStatus().name() : null
                ));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch project managers");
        }
    }

    @Override
    public ProjectSummaryDTO getProjectSummary(String projectId) throws RemoteException {
        // Stub for now. Full implementation will aggregate costs and workers in ReportServiceImpl
        return new ProjectSummaryDTO();
    }
}
