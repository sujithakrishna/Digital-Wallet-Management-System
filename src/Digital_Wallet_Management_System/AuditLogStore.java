package Digital_Wallet_Management_System;

import java.util.ArrayList;
import java.util.List;

public class AuditLogStore {

    private static List<AuditLog> auditLogs =
            new ArrayList<>();

    private static int auditCounter = 1000;

    private AuditLogStore() {
        // Prevent object creation
    }

    public static String generateAuditId() {

        auditCounter++;

        return "AUDIT" + auditCounter;
    }

    public static void addAuditLog(AuditLog auditLog) {

        if (auditLog != null) {
            auditLogs.add(auditLog);
        }
    }

    public static List<AuditLog> getAuditLogsByUserId(
            String userId) {

        List<AuditLog> userAuditLogs =
                new ArrayList<>();

        for (AuditLog auditLog : auditLogs) {

            if (userId.equals(auditLog.getUserId())) {
                userAuditLogs.add(auditLog);
            }
        }

        return userAuditLogs;
    }

    public static List<AuditLog> getAllAuditLogs() {

        return new ArrayList<>(auditLogs);
    }

    public static int getAuditLogCount() {

        return auditLogs.size();
    }
}