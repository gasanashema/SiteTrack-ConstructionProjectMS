package service.implementation;

import dao.ProjectActivityDao;
import dao.ProjectDao;
import dao.UserDao;
import dto.ProjectActivityDTO;
import model.ProjectActivity;
import model.Project;
import model.User;
import service.interfaces.ProjectActivityService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivityServiceImpl extends UnicastRemoteObject implements ProjectActivityService {
    private final ProjectActivityDao dao;
    private final ProjectDao projectDao;
    private final UserDao userDao;

    public ProjectActivityServiceImpl() throws RemoteException {
        super();
        this.dao = new ProjectActivityDao();
        this.projectDao = new ProjectDao();
        this.userDao = new UserDao();
    }

    private ProjectActivityDTO toDTO(ProjectActivity entity) {
        if (entity == null) return null;
        return new ProjectActivityDTO(
            entity.getId(),
            entity.getProject() != null ? entity.getProject().getId() : null,
            entity.getProject() != null ? entity.getProject().getProjectName() : null,
            entity.getActivityTitle(),
            entity.getActivityDescription(),
            entity.getActivityDate(),
            entity.getProgressPercentage(),
            entity.getRecordedBy() != null ? entity.getRecordedBy().getFullName() : null
        );
    }

    @Override
    public ProjectActivityDTO createActivity(ProjectActivityDTO dto) throws RemoteException {
        try {
            ProjectActivity entity = new ProjectActivity();
            entity.setActivityTitle(dto.getActivityTitle());
            entity.setActivityDescription(dto.getActivityDescription());
            entity.setActivityDate(dto.getActivityDate());
            entity.setProgressPercentage(dto.getProgressPercentage());
            
            // Map relationships
            Project p = projectDao.findById(dto.getProjectId());
            if(p != null) entity.setProject(p);
            
            // Using a stub recordedBy since we don't have the user ID explicitly in the DTO unless we assume recordedByName holds ID or we pass it
            // Assuming we pass userId in recordedByName for creation
            User u = userDao.findById(dto.getRecordedByName()); 
            if(u != null) entity.setRecordedBy(u);

            entity = dao.save(entity);
            
            // Project's overall progress is derived from its activities - no direct field to update
            if (p != null) {
                projectDao.update(p);
            }
            
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create project activity");
        }
    }

    @Override
    public ProjectActivityDTO updateActivity(ProjectActivityDTO dto) throws RemoteException {
        try {
            ProjectActivity entity = dao.findById(dto.getId());
            if (entity == null) throw new IllegalArgumentException("Activity not found");
            
            entity.setActivityTitle(dto.getActivityTitle());
            entity.setActivityDescription(dto.getActivityDescription());
            entity.setActivityDate(dto.getActivityDate());
            entity.setProgressPercentage(dto.getProgressPercentage());
            
            entity = dao.update(entity);
            
            // Project's overall progress is derived from its activities - no direct field to update
            Project p = entity.getProject();
            if (p != null) {
                projectDao.update(p);
            }
            
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update project activity");
        }
    }

    @Override
    public boolean deleteActivity(String activityId) throws RemoteException {
        try {
            return dao.delete(activityId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete project activity");
        }
    }

    @Override
    public ProjectActivityDTO getActivityById(String activityId) throws RemoteException {
        try {
            return toDTO(dao.findById(activityId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch project activity");
        }
    }

    @Override
    public List<ProjectActivityDTO> getActivitiesByProject(String projectId) throws RemoteException {
        try {
            List<ProjectActivityDTO> list = new ArrayList<>();
            for (ProjectActivity a : dao.findByProject(projectId)) {
                list.add(toDTO(a));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch project activities");
        }
    }

    @Override
    public List<ProjectActivityDTO> getActivitiesByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        try {
            List<ProjectActivityDTO> list = new ArrayList<>();
            for (ProjectActivity a : dao.findByProjectAndDateRange(projectId, from, to)) {
                list.add(toDTO(a));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch project activities by date");
        }
    }

    @Override
    public ProjectActivityDTO getLatestActivityByProject(String projectId) throws RemoteException {
        try {
            List<ProjectActivity> activities = dao.findByProject(projectId);
            if(activities.isEmpty()) return null;
            return toDTO(activities.get(0)); // Assuming sorted desc by date in DAO
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch latest project activity");
        }
    }
}
