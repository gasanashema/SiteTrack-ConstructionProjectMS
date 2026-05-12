package dto;

import java.io.Serializable;

public class WorkerTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String typeName;
    private java.math.BigDecimal defaultDailyRate;
    private String description;

    public WorkerTypeDTO() {}

    public WorkerTypeDTO(String id, String typeName, java.math.BigDecimal defaultDailyRate, String description) {
        this.id = id;
        this.typeName = typeName;
        this.defaultDailyRate = defaultDailyRate;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public java.math.BigDecimal getDefaultDailyRate() { return defaultDailyRate; }
    public void setDefaultDailyRate(java.math.BigDecimal defaultDailyRate) { this.defaultDailyRate = defaultDailyRate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
