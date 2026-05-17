package util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportHistoryManager {
    private static ReportHistoryManager instance;
    private final Map<String, CachedReport> reportCache;
    private final List<ReportHistoryEntry> history;
    private static final int CACHE_EXPIRY_MINUTES = 30;

    private ReportHistoryManager() {
        // Use LinkedHashMap for predictable iteration order
        this.reportCache = new LinkedHashMap<>();
        this.history = new ArrayList<>();
    }

    public static synchronized ReportHistoryManager getInstance() {
        if (instance == null) {
            instance = new ReportHistoryManager();
        }
        return instance;
    }

    public static class CachedReport {
        private final Object reportData;
        private final LocalDateTime generatedAt;
        private final String reportName;

        public CachedReport(Object reportData, String reportName) {
            this.reportData = reportData;
            this.reportName = reportName;
            this.generatedAt = LocalDateTime.now();
        }

        public boolean isExpired() {
            return LocalDateTime.now().minusMinutes(CACHE_EXPIRY_MINUTES).isAfter(generatedAt);
        }

        public Object getReportData() {
            return reportData;
        }

        public String getReportName() {
            return reportName;
        }

        public LocalDateTime getGeneratedAt() {
            return generatedAt;
        }
    }

    public static class ReportHistoryEntry {
        private final String reportName;
        private final String cacheKey;
        private final LocalDateTime generatedAt;

        public ReportHistoryEntry(String reportName, String cacheKey, LocalDateTime generatedAt) {
            this.reportName = reportName;
            this.cacheKey = cacheKey;
            this.generatedAt = generatedAt;
        }

        public String getReportName() { return reportName; }
        public String getCacheKey() { return cacheKey; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
    }

    public synchronized void cacheReport(String key, Object reportData, String reportName) {
        CachedReport cached = new CachedReport(reportData, reportName);
        reportCache.put(key, cached);
        
        history.add(0, new ReportHistoryEntry(reportName, key, cached.getGeneratedAt()));
        
        // Keep only last 20 entries in history
        if (history.size() > 20) {
            ReportHistoryEntry removed = history.remove(history.size() - 1);
            // Optionally remove from cache if not referenced by newer history, 
            // but we let it expire naturally or be overwritten
        }
    }

    public synchronized Object getCachedReport(String key) {
        CachedReport cached = reportCache.get(key);
        if (cached != null) {
            if (cached.isExpired()) {
                reportCache.remove(key);
                return null;
            }
            return cached.getReportData();
        }
        return null;
    }

    public synchronized List<ReportHistoryEntry> getReportHistory() {
        return new ArrayList<>(history);
    }

    public synchronized void clearCache() {
        reportCache.clear();
    }

    public synchronized void clearHistory() {
        history.clear();
    }
}
