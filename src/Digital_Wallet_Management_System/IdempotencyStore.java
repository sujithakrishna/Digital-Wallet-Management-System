package Digital_Wallet_Management_System;

import java.util.HashMap;
import java.util.Map;

public class IdempotencyStore {

    private static Map<String, String> processedRequests =
            new HashMap<>();

    private IdempotencyStore() {
        // Prevent object creation
    }

    public static boolean requestExists(String requestKey) {

        return processedRequests.containsKey(requestKey);
    }

    public static void saveRequest(
            String requestKey,
            String transactionId) {

        if(requestKey == null || transactionId == null) {
            return;
        }

        processedRequests.put(
                requestKey,
                transactionId
        );
    }

    public static String getTransactionId(
            String requestKey) {

        return processedRequests.get(requestKey);
    }

    public static int getProcessedRequestCount() {

        return processedRequests.size();
    }
}


