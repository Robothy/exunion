package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.util.Key;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;

import java.math.BigDecimal;

public class HuobiSpotOrderDetail extends HuobiSpotOrder {

    @Key(Keys.ORDER_ID)
    private Long orderId;

    @Key(Keys.ORDER_STATE)
    private Integer orderState;

    @Key(Keys.CREATED_AT)
    private Long createdAt;

    @Key(Keys.FILLED_AMOUNT)
    private BigDecimal filledAmount;

    @Key(Keys.FILLED_CASH_AMOUNT)
    private BigDecimal filledCashAmount;

    @Key(Keys.FILLED_FEES)
    private BigDecimal filledFees;

    @Key(Keys.STATE)
    private String state;

    public SpotOrderDetails toSpotOrderDetail(){
        SpotOrderDetails spotOrderDetails = new SpotOrderDetails();
        spotOrderDetails.copyPropertiesFromSpotOrder(this.toSpotOrder());
        spotOrderDetails.setOrderId(String.valueOf(this.orderId));
        if(this.orderState != null){
            HuobiOrderStatus huobiOrderStatus = HuobiOrderStatus.of(this.orderState);
            if(null != huobiOrderStatus) spotOrderDetails.setStatus(huobiOrderStatus.getStatus());
        }
        spotOrderDetails.setExecutedAmount(filledCashAmount);
        spotOrderDetails.setExecutedQuantity(filledAmount);
        return spotOrderDetails;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getFilledAmount() {
        return filledAmount;
    }

    public void setFilledAmount(BigDecimal filledAmount) {
        this.filledAmount = filledAmount;
    }

    public BigDecimal getFilledCashAmount() {
        return filledCashAmount;
    }

    public void setFilledCashAmount(BigDecimal filledCashAmount) {
        this.filledCashAmount = filledCashAmount;
    }

    public BigDecimal getFilledFees() {
        return filledFees;
    }

    public void setFilledFees(BigDecimal filledFees) {
        this.filledFees = filledFees;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    interface Keys{
        String ORDER_ID = "order-id";
        String ORDER_STATE = "order-state";
        String CREATED_AT = "created-at";
        String FILLED_AMOUNT = "filled-amount";
        String FILLED_CASH_AMOUNT = "filled-cash-amount";
        String FILLED_FEES = "filled-fees";
        String STATE = "state";
    }

}
