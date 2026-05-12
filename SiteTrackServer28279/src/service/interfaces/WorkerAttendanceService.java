package service.interfaces;

import dto.WorkerAttendanceDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface WorkerAttendanceService extends Remote {
    WorkerAttendanceDTO recordAttendance(WorkerAttendanceDTO dto) throws RemoteException;
    WorkerAttendanceDTO updateAttendance(WorkerAttendanceDTO dto) throws RemoteException;
    boolean deleteAttendance(String attendanceId) throws RemoteException;
    WorkerAttendanceDTO getAttendanceById(String attendanceId) throws RemoteException;
    List<WorkerAttendanceDTO> getAttendanceByProject(String projectId) throws RemoteException;
    List<WorkerAttendanceDTO> getAttendanceByProjectAndDate(String projectId, LocalDate workDate) throws RemoteException;
    List<WorkerAttendanceDTO> getPresentWorkersByProjectAndDate(String projectId, LocalDate workDate) throws RemoteException;
    List<WorkerAttendanceDTO> getAttendanceByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException;
}
