package com.robothy.exunion.core.trade.spot;

import com.robothy.exunion.core.meta.Symbol;

import java.math.BigDecimal;
import java.util.HashMap;
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

    public static class Builder {

        private final SpotOrder spotOrder;

        private Builder(){
            spotOrder = new SpotOrder();
        }

        public static Builder create(){
            return new Builder();
        }

        public Builder side(Side side){
            spotOrder.side = side;
            return this;
        }

        public Builder type(Type type){
            spotOrder.type = type;
            return this;
        }

        public Builder symbol(Symbol symbol){
            spotOrder.symbol = symbol;
            return this;
        }

        public Builder quantity(BigDecimal quantity){
            spotOrder.quantity = quantity;
            return this;
        }

        public Builder price(BigDecimal price){
            spotOrder.price = price;
            return this;
        }

        /**
         * Add an extra property.
         * @param key the property key.
         * @param value the property value.
         *
         */
        public Builder extra(String key, Object value){
            if(this.spotOrder.extraInfo == null){
                this.spotOrder.extraInfo = new HashMap<>();
            }
            spotOrder.extraInfo.put(key, value);
            return this;
        }

        public SpotOrder build(){
            return this.spotOrder;
        }
    }
}
