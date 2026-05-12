package service.interfaces;

import dto.ProjectActivityDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ProjectActivityService extends Remote {
    ProjectActivityDTO createActivity(ProjectActivityDTO dto) throws RemoteException;
    ProjectActivityDTO updateActivity(ProjectActivityDTO dto) throws RemoteException;
    boolean deleteActivity(String activityId) throws RemoteException;
    ProjectActivityDTO getActivityById(String activityId) throws RemoteException;
    List<ProjectActivityDTO> getActivitiesByProject(String projectId) throws RemoteException;
    List<ProjectActivityDTO> getActivitiesByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException;
    ProjectActivityDTO getLatestActivityByProject(String projectId) throws RemoteException;
}
