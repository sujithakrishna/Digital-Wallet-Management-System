package Digital_Wallet_Management_System;

import java.util.ArrayList;
import java.util.List;

public class BeneficiaryStore {

    private static List<Beneficiary> beneficiaries =
            new ArrayList<>();

    private static int beneficiaryCounter = 1000;

    public static void addBeneficiary(Beneficiary beneficiary) {

        beneficiaries.add(beneficiary);
    }

    public static String generateBeneficiaryId() {

        beneficiaryCounter++;

        return "BEN" + beneficiaryCounter;
    }

    public static List<Beneficiary> getBeneficiariesByUserId(
            String userId) {

        List<Beneficiary> userBeneficiaries =
                new ArrayList<>();

        for (Beneficiary beneficiary : beneficiaries) {

            if (beneficiary.getUserId().equals(userId)) {

                userBeneficiaries.add(beneficiary);
            }
        }

        return userBeneficiaries;
    }

    public static Beneficiary getBeneficiaryById(
            String beneficiaryId) {

        for (Beneficiary beneficiary : beneficiaries) {

            if (beneficiary.getBeneficiaryId()
                    .equals(beneficiaryId)) {

                return beneficiary;
            }
        }

        return null;
    }

    public static boolean beneficiaryExists(
            String userId,
            String beneficiaryUserId) {

        for (Beneficiary beneficiary : beneficiaries) {

            if (beneficiary.getUserId().equals(userId)
                    && beneficiary.getBeneficiaryUserId()
                    .equals(beneficiaryUserId)) {

                return true;
            }
        }

        return false;
    }

    public static void removeBeneficiary(
            String beneficiaryId) {

        Beneficiary beneficiary =
                getBeneficiaryById(beneficiaryId);

        if (beneficiary != null) {

            beneficiaries.remove(beneficiary);
        }
    }
}