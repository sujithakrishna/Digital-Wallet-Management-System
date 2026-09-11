package Digital_Wallet_Management_System;

public class IdempotencyService {

    private IdempotencyService() {
        // Prevent object creation
    }

    public static boolean isAlreadyProcessed(
            String requestKey) {

        if(requestKey == null ||
           requestKey.trim().isEmpty()) {

            return false;
        }

        return IdempotencyStore.requestExists(
                requestKey
        );
    }

    public static void recordProcessedRequest(
            String requestKey,
            String transactionId) {

        if(requestKey == null ||
           requestKey.trim().isEmpty()) {

            return;
        }

        if(transactionId == null ||
           transactionId.trim().isEmpty()) {

            return;
        }

        IdempotencyStore.saveRequest(
                requestKey,
                transactionId
        );
    }

    public static String getExistingTransactionId(
            String requestKey) {

        if(requestKey == null ||
           requestKey.trim().isEmpty()) {

            return null;
        }

        return IdempotencyStore.getTransactionId(
                requestKey
        );
    }
}