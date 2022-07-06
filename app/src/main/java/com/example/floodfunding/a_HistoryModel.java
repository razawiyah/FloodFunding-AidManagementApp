package com.example.floodfunding;

public class a_HistoryModel {
    public a_HistoryModel(){}

    String amount, payDate, ic;

    public a_HistoryModel(String amount, String payDate, String ic) {
        this.amount = amount;
        this.payDate = payDate;
        this.ic = ic;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getIc() {
        return ic;
    }

    public void setPIc(String ic) {
        this.ic = ic;
    }
}
