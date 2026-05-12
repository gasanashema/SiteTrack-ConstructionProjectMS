package service.interfaces;

import dto.ProjectDTO;
import dto.ProjectManagerDTO;
import dto.ProjectSummaryDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ProjectService extends Remote {
    ProjectDTO createProject(ProjectDTO projectDTO) throws RemoteException;
    ProjectDTO updateProject(ProjectDTO projectDTO) throws RemoteException;
    boolean deleteProject(String projectId) throws RemoteException;
    boolean changeProjectStatus(String projectId, String status) throws RemoteException;
    ProjectDTO getProjectById(String projectId) throws RemoteException;
    List<ProjectDTO> getAllProjects() throws RemoteException;
    List<ProjectDTO> getProjectsByStatus(String status) throws RemoteException;
    List<ProjectDTO> getProjectsManagedBy(String userId) throws RemoteException;
    boolean assignManager(String projectId, String userId) throws RemoteException;
    boolean removeManager(String projectId, String userId) throws RemoteException;
    List<ProjectManagerDTO> getManagersByProject(String projectId) throws RemoteException;
    ProjectSummaryDTO getProjectSummary(String projectId) throws RemoteException;
}
