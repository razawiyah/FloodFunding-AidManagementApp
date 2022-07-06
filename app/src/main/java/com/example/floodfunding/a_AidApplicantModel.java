package com.example.floodfunding;

public class a_AidApplicantModel {
    public a_AidApplicantModel(){}

    String ic, date, approvalStat;

    public a_AidApplicantModel(String ic, String date, String approvalStat) {
        this.ic = ic;
        this.date = date;
        this.approvalStat = approvalStat;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApprovalStat() {
        return approvalStat;
    }

    public void setApprovalStat(String approvalStat) {
        this.approvalStat = approvalStat;
    }
}
