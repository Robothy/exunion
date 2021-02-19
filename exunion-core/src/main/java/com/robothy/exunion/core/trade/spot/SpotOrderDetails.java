package com.robothy.exunion.core.trade.spot;

import java.math.BigDecimal;

public class SpotOrderDetails extends SpotOrder {

    /**
     * Order status
     */
    private Status status = null;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * executed amount(quote currency).
     */
    private BigDecimal executedAmount;

    public BigDecimal getExecutedAmount() {
        return executedAmount;
    }

    public void setExecutedAmount(BigDecimal executedAmount) {
        this.executedAmount = executedAmount;
    }

    /**
     * executed quantity(base currency)
     */
    private BigDecimal executedQuantity;

    public BigDecimal getExecutedQuantity() {
        return executedQuantity;
    }

    public void setExecutedQuantity(BigDecimal executedQuantity) {
        this.executedQuantity = executedQuantity;
    }

    /**
     * commission timestamp
     */
    private Long commissionTimestamp;

    public Long getCommissionTimestamp() {
        return commissionTimestamp;
    }

    public void setCommissionTimestamp(Long commissionTimestamp) {
        this.commissionTimestamp = commissionTimestamp;
    }

    /**
     * The order status enumeration
     */
    public enum Status {
        NEW,

        PARTIALLY_FILLED,

        FILLED,

        CANCELED,

        PENDING_CANCEL,

        REJECTED,

        EXPIRED
    }

}
