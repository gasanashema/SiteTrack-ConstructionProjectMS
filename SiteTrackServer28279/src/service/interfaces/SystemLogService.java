package service.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SystemLogService extends Remote {
    List<String[]> getRecentLogs(int maxLines) throws RemoteException;
}
