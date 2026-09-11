package Digital_Wallet_Management_System;

public class Beneficiary {

    private String beneficiaryId;
    private String userId;
    private String beneficiaryUserId;
    private String beneficiaryName;
    private String beneficiaryEmail;

    public Beneficiary(String beneficiaryId,
                       String userId,
                       String beneficiaryUserId,
                       String beneficiaryName,
                       String beneficiaryEmail) {

        this.beneficiaryId = beneficiaryId;
        this.userId = userId;
        this.beneficiaryUserId = beneficiaryUserId;
        this.beneficiaryName = beneficiaryName;
        this.beneficiaryEmail = beneficiaryEmail;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public String getUserId() {
        return userId;
    }

    public String getBeneficiaryUserId() {
        return beneficiaryUserId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }
}