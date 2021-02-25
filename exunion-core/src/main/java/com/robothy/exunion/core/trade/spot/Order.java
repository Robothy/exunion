package com.robothy.exunion.core.trade.spot;

import com.robothy.exunion.core.meta.Symbol;

import java.math.BigDecimal;
import java.util.Map;

public class Order {

    /**
     * Order status
     */
    private Status status = null;

    /**
     * Order side, buy or sell
     */
    private Side side = null;

    /**
     * Order type, limit, market
     */
    private Type type = null;

    /**
     * symbol of this order, format: {base currency}_{quote currency}.
     */
    private Symbol symbol = null;

    /**
     * the quantity of base currency of this order
     */
    private BigDecimal quantity = null;
    /**
     * the price of this order
     */
    private BigDecimal price = null;

    /**
     * executed amount(quote currency).
     */
    private BigDecimal executedAmount;

    /**
     * executed quantity(base currency)
     */
    private BigDecimal executedQuantity;

    /**
     * order ID
     */
    private String orderId = null;

    /**
     * commission timestamp
     */
    private Long commissionTimestamp;

    /**
     * Extra information
     */
    private Map<String, Object> extraInfo;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }


    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getCommissionTimestamp() {
        return commissionTimestamp;
    }

    public void setCommissionTimestamp(Long commissionTimestamp) {
        this.commissionTimestamp = commissionTimestamp;
    }



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
