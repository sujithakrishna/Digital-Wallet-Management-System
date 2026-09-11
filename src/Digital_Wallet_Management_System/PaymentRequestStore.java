package Digital_Wallet_Management_System;

import java.util.ArrayList;
import java.util.List;

public class PaymentRequestStore {

    private static List<PaymentRequest> paymentRequests =
            new ArrayList<>();

    private static int requestCounter = 1000;


    // ========================================================
    // GENERATE REQUEST ID
    // ========================================================

    public static String generateRequestId()
    {
        requestCounter++;

        return "REQ" + requestCounter;
    }


    // ========================================================
    // ADD PAYMENT REQUEST
    // ========================================================

    public static void addPaymentRequest(
            PaymentRequest paymentRequest)
    {
        paymentRequests.add(paymentRequest);
    }


    // ========================================================
    // GET REQUEST BY ID
    // ========================================================

    public static PaymentRequest getPaymentRequestById(
            String requestId)
    {
        for(PaymentRequest request : paymentRequests)
        {
            if(request.getRequestId().equals(requestId))
            {
                return request;
            }
        }

        return null;
    }


    // ========================================================
    // GET REQUESTS CREATED BY USER
    // ========================================================

    public static List<PaymentRequest> getRequestsByRequesterId(
            String userId)
    {
        List<PaymentRequest> userRequests =
                new ArrayList<>();

        for(PaymentRequest request : paymentRequests)
        {
            if(request.getRequesterUserId().equals(userId))
            {
                userRequests.add(request);
            }
        }

        return userRequests;
    }


    // ========================================================
    // GET PENDING REQUESTS FOR PAYER
    // ========================================================

    public static List<PaymentRequest> getPendingRequestsForPayer(
            String userId)
    {
        List<PaymentRequest> pendingRequests =
                new ArrayList<>();

        for(PaymentRequest request : paymentRequests)
        {
            if(request.getPayerUserId().equals(userId)
                    && request.getStatus().equals("PENDING"))
            {
                pendingRequests.add(request);
            }
        }

        return pendingRequests;
    }


    // ========================================================
    // CHECK DUPLICATE PENDING REQUEST
    // ========================================================

    public static boolean pendingRequestExists(
            String requesterUserId,
            String payerUserId)
    {
        for(PaymentRequest request : paymentRequests)
        {
            if(request.getRequesterUserId().equals(requesterUserId)
                    && request.getPayerUserId().equals(payerUserId)
                    && request.getStatus().equals("PENDING"))
            {
                return true;
            }
        }

        return false;
    }


    // ========================================================
    // GET TOTAL REQUEST COUNT
    // ========================================================

    public static int getPaymentRequestCount()
    {
        return paymentRequests.size();
    }
}