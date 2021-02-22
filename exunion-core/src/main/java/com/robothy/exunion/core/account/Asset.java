package com.robothy.exunion.core.account;

import java.math.BigDecimal;

public class Asset {

    private BigDecimal freeAmount;

    private BigDecimal lockedAmount;

    private String currency;

    public Asset(String currency){
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    public BigDecimal getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(BigDecimal lockedAmount) {
        this.lockedAmount = lockedAmount;
    }
}
