package com.example.floodfunding;

public class c_HistoryModel {
    public c_HistoryModel(){}

    String amount, payDate, payStat;

    public c_HistoryModel(String amount, String payDate, String payStat) {
        this.amount = amount;
        this.payDate = payDate;
        this.payStat = payStat;
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

    public String getPayStat() {
        return payStat;
    }

    public void setPayStat(String payStat) {
        this.payStat = payStat;
    }
}
