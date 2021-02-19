package com.robothy.exunion.core.trade.spot;

import com.robothy.exunion.core.meta.Symbol;

import java.math.BigDecimal;
import java.util.Map;

public class SpotOrder {

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
    private Symbol symbol = null;

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
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
     * Extra information
     */
    private Map<String, Object> extraInfo;

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

    public enum Type {
        LIMIT, MARKET
    }

}
