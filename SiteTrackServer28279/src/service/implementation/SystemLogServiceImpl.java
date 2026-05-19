package service.implementation;

import service.interfaces.SystemLogService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemLogServiceImpl extends UnicastRemoteObject implements SystemLogService {
    
    private final String logFilePath = "logs/server.log";

    public SystemLogServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<String[]> getRecentLogs(int maxLines) throws RemoteException {
        List<String[]> logs = new ArrayList<>();
        File file = new File(logFilePath);
        
        if (!file.exists()) {
            return logs;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> rawLines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                rawLines.add(line);
            }
            
            // We want the most recent lines first
            Collections.reverse(rawLines);
            
            int count = 0;
            for (String raw : rawLines) {
                if (count >= maxLines) break;
                
                // Expected format from our custom formatter:
                // [2026-05-18 10:00:00] [INFO] [LoggerName] Message||Exception
                try {
                    if (raw.startsWith("[")) {
                        int timeEnd = raw.indexOf("]");
                        int levelStart = raw.indexOf("[", timeEnd);
                        int levelEnd = raw.indexOf("]", levelStart);
                        int loggerStart = raw.indexOf("[", levelEnd);
                        int loggerEnd = raw.indexOf("]", loggerStart);
                        
                        if (timeEnd != -1 && levelStart != -1 && loggerStart != -1) {
                            String time = raw.substring(1, timeEnd);
                            String level = raw.substring(levelStart + 1, levelEnd);
                            String logger = raw.substring(loggerStart + 1, loggerEnd);
                            String rest = raw.substring(loggerEnd + 2).trim();
                            
                            String msg = rest;
                            String exc = "";
                            int excIdx = rest.indexOf("||");
                            if (excIdx != -1) {
                                msg = rest.substring(0, excIdx);
                                exc = rest.substring(excIdx + 2);
                            }
                            
                            logs.add(new String[]{time, level, logger, msg, exc});
                            count++;
                        }
                    }
                } catch (Exception ex) {
                    // Skip unparseable lines
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return logs;
    }
}
