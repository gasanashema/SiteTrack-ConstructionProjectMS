package service.implementation;

import dao.WorkerAttendanceDao;
import dao.ProjectDao;
import dao.SiteWorkerDao;
import dao.UserDao;
import dto.WorkerAttendanceDTO;
import model.WorkerAttendance;
import model.Project;
import model.SiteWorker;
import model.User;
import model.EAttendanceStatus;
import service.interfaces.WorkerAttendanceService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkerAttendanceServiceImpl extends UnicastRemoteObject implements WorkerAttendanceService {
    private final WorkerAttendanceDao dao;
    private final ProjectDao projectDao;
    private final SiteWorkerDao siteWorkerDao;
    private final UserDao userDao;

    public WorkerAttendanceServiceImpl() throws RemoteException {
        super();
        this.dao = new WorkerAttendanceDao();
        this.projectDao = new ProjectDao();
        this.siteWorkerDao = new SiteWorkerDao();
        this.userDao = new UserDao();
    }

    private WorkerAttendanceDTO toDTO(WorkerAttendance entity) {
        if (entity == null) return null;
        return new WorkerAttendanceDTO(
            entity.getId(),
            entity.getProject() != null ? entity.getProject().getId() : null,
            entity.getProject() != null ? entity.getProject().getProjectName() : null,
            entity.getWorker() != null ? entity.getWorker().getId() : null,
            entity.getWorker() != null ? entity.getWorker().getFullName() : null,
            entity.getWorker() != null && entity.getWorker().getWorkerType() != null ? entity.getWorker().getWorkerType().getTypeName() : null,
            entity.getWorkDate(),
            entity.getAttendanceStatus().name(),
            entity.getWorkDescription(),
            entity.getRecordedBy() != null ? entity.getRecordedBy().getFullName() : null
        );
    }

    @Override
    public WorkerAttendanceDTO recordAttendance(WorkerAttendanceDTO dto) throws RemoteException {
        try {
            WorkerAttendance entity = new WorkerAttendance();
            entity.setWorkDate(dto.getWorkDate());
            entity.setAttendanceStatus(EAttendanceStatus.valueOf(dto.getAttendanceStatus()));
            entity.setWorkDescription(dto.getWorkDescription());
            
            Project p = projectDao.findById(dto.getProjectId());
            if (p != null) entity.setProject(p);
            
            SiteWorker w = siteWorkerDao.findById(dto.getWorkerId());
            if (w != null) entity.setWorker(w);
            
            User u = userDao.findById(dto.getRecordedByName()); // Assuming username/id is passed here
            if (u != null) entity.setRecordedBy(u);
            
            entity = dao.save(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to record attendance");
        }
    }

    @Override
    public WorkerAttendanceDTO updateAttendance(WorkerAttendanceDTO dto) throws RemoteException {
        try {
            WorkerAttendance entity = dao.findById(dto.getId());
            if (entity == null) throw new IllegalArgumentException("Attendance not found");
            
            entity.setWorkDate(dto.getWorkDate());
            entity.setAttendanceStatus(EAttendanceStatus.valueOf(dto.getAttendanceStatus()));
            entity.setWorkDescription(dto.getWorkDescription());
            
            entity = dao.update(entity);
            return toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update attendance");
        }
    }

    @Override
    public boolean deleteAttendance(String attendanceId) throws RemoteException {
        try {
            return dao.delete(attendanceId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete attendance");
        }
    }

    @Override
    public WorkerAttendanceDTO getAttendanceById(String attendanceId) throws RemoteException {
        try {
            return toDTO(dao.findById(attendanceId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch attendance");
        }
    }

    @Override
    public List<WorkerAttendanceDTO> getAttendanceByProject(String projectId) throws RemoteException {
        try {
            List<WorkerAttendanceDTO> list = new ArrayList<>();
            for (WorkerAttendance a : dao.findByProject(projectId)) {
                list.add(toDTO(a));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch attendances");
        }
    }

    @Override
    public List<WorkerAttendanceDTO> getAttendanceByProjectAndDate(String projectId, LocalDate workDate) throws RemoteException {
        try {
            List<WorkerAttendanceDTO> list = new ArrayList<>();
            for (WorkerAttendance a : dao.findByProjectAndDate(projectId, workDate)) {
                list.add(toDTO(a));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch attendances by date");
        }
    }

    @Override
    public List<WorkerAttendanceDTO> getPresentWorkersByProjectAndDate(String projectId, LocalDate workDate) throws RemoteException {
        try {
            List<WorkerAttendanceDTO> list = new ArrayList<>();
            for (WorkerAttendance a : dao.findByProjectAndDate(projectId, workDate)) {
                if(a.getAttendanceStatus() == EAttendanceStatus.PRESENT) {
                    list.add(toDTO(a));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch present workers");
        }
    }

    @Override
    public List<WorkerAttendanceDTO> getAttendanceByProjectAndDateRange(String projectId, LocalDate from, LocalDate to) throws RemoteException {
        try {
            List<WorkerAttendanceDTO> list = new ArrayList<>();
            for (WorkerAttendance a : dao.findByProjectAndDateRange(projectId, from, to)) {
                list.add(toDTO(a));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch attendances by date range");
        }
    }
}
