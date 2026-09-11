package Digital_Wallet_Management_System;

public class AuditLog {

    private String auditId;
    private String userId;
    private String action;
    private String description;
    private String dateTime;

    public AuditLog(String auditId,
                    String userId,
                    String action,
                    String description,
                    String dateTime) {

        this.auditId = auditId;
        this.userId = userId;
        this.action = action;
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getAuditId() {
        return auditId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public String getDateTime() {
        return dateTime;
    }
}