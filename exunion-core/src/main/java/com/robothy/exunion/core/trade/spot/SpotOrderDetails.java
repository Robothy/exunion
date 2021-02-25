package com.robothy.exunion.core.trade.spot;

import java.math.BigDecimal;

public class SpotOrderDetails extends SpotOrder {

    /**
     * Order status
     */
    private Status status = null;

    /**
     * executed amount(quote currency).
     */
    private BigDecimal executedAmount;

    /**
     * executed quantity(base currency)
     */
    private BigDecimal executedQuantity;

    /**
     * commission timestamp
     */
    private Long commissionTimestamp;

    /**
     * order ID
     */
    private String orderId = null;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getExecutedAmount() {
        return executedAmount;
    }

    public void setExecutedAmount(BigDecimal executedAmount) {
        this.executedAmount = executedAmount;
    }

    public BigDecimal getExecutedQuantity() {
        return executedQuantity;
    }

    public void setExecutedQuantity(BigDecimal executedQuantity) {
        this.executedQuantity = executedQuantity;
    }

    public Long getCommissionTimestamp() {
        return commissionTimestamp;
    }

    public void setCommissionTimestamp(Long commissionTimestamp) {
        this.commissionTimestamp = commissionTimestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * a util method that help to copy properties from a spot order instance to spot order detail instance.
     * @param spotOrder a spot order instance
     */
    public void copyPropertiesFromSpotOrder(SpotOrder spotOrder){
        this.setSide(spotOrder.getSide());
        this.setType(spotOrder.getType());
        this.setSymbol(spotOrder.getSymbol());
        this.setPrice(spotOrder.getPrice());
        this.setQuantity(spotOrder.getQuantity());
        this.setExtraInfo(spotOrder.getExtraInfo());
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
