package Digital_Wallet_Management_System;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AuditLogService {

    private AuditLogService() {
        // Prevent object creation
    }

    public static void log(String userId,
                           String action,
                           String description) {

        String auditId =
                AuditLogStore.generateAuditId();

        String dateTime =
                getCurrentDateTime();

        AuditLog auditLog =
                new AuditLog(
                        auditId,
                        userId,
                        action,
                        description,
                        dateTime
                );

        AuditLogStore.addAuditLog(auditLog);
    }

    private static String getCurrentDateTime() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy HH:mm:ss"
                );

        return Instant.now()
                .atZone(ZoneId.systemDefault())
                .format(formatter);
    }

    public static void viewUserAuditLogs(User user) {

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        List<AuditLog> auditLogs =
                AuditLogStore.getAuditLogsByUserId(
                        user.getUserId()
                );

        System.out.println();
        System.out.println("========================================================");
        System.out.println("                    AUDIT LOGS");
        System.out.println("========================================================");

        if (auditLogs.isEmpty()) {

            System.out.println("No audit logs found.");

            System.out.println("========================================================");

            return;
        }

        for (AuditLog auditLog : auditLogs) {

            System.out.println(
                    "Audit ID     : "
                    + auditLog.getAuditId()
            );

            System.out.println(
                    "Action       : "
                    + auditLog.getAction()
            );

            System.out.println(
                    "Description  : "
                    + auditLog.getDescription()
            );

            System.out.println(
                    "Date & Time  : "
                    + auditLog.getDateTime()
            );

            System.out.println("--------------------------------------------------------");
        }

        System.out.println("========================================================");
    }

    public static void viewAllAuditLogs() {

        List<AuditLog> auditLogs =
                AuditLogStore.getAllAuditLogs();

        System.out.println();
        System.out.println("========================================================");
        System.out.println("                ALL AUDIT LOGS");
        System.out.println("========================================================");

        if (auditLogs.isEmpty()) {

            System.out.println("No audit logs found.");

            System.out.println("========================================================");

            return;
        }

        for (AuditLog auditLog : auditLogs) {

            System.out.println(
                    "Audit ID     : "
                    + auditLog.getAuditId()
            );

            System.out.println(
                    "User ID      : "
                    + auditLog.getUserId()
            );

            System.out.println(
                    "Action       : "
                    + auditLog.getAction()
            );

            System.out.println(
                    "Description  : "
                    + auditLog.getDescription()
            );

            System.out.println(
                    "Date & Time  : "
                    + auditLog.getDateTime()
            );

            System.out.println("--------------------------------------------------------");
        }

        System.out.println("========================================================");
    }
}