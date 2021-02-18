package com.robothy.exunion.core.trade.spot;

import java.math.BigDecimal;
import java.util.Map;

public class Order {

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
     * Order side, buy or sell
     */
    private Side side = null;

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    /**
     * Order type, limit, market
     */
    private Type type = null;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * symbol of this order, format: {base currency}_{quote currency}.
     */
    private String symbol = null;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * the quantity of base currency of this order
     */
    private BigDecimal quantity = null;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * the price of this order
     */
    private BigDecimal price = null;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
     * order ID
     */
    private String orderId = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
     * Extra information
     */
    public Map<String, Object> extraInfo;

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    /**
     * Order side.
     */
    public enum Side {
        BUY, SELL
    }

    public enum Type{
        LIMIT, MARKET
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
