package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.util.Key;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.huobi.market.HuobiSymbol;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

public class HuobiSpotOrder {

    public HuobiSpotOrder() {
    }

    /**
     * Create an HhuobiOrder instance by Order.
     * @param order the standard order.
     */
    public HuobiSpotOrder(SpotOrder order) {
        if(null == order) return;
        Map<String, Object> extra = order.getExtraInfo() == null ? Collections.emptyMap() : order.getExtraInfo();
        this.accountId = (String) extra.get("account-id");
        this.symbol = order.getSymbol() == null ? null : HuobiSymbol.of(order.getSymbol()).toString();
        this.type = (order.getSide() == null || order.getType() == null) ? null : HuobiOrderType.of(order.getSide(), order.getType());
        this.amount = order.getQuantity();
        this.price = order.getPrice();
        this.source = (String) extra.get("source");
        this.clientOrderId = (String) extra.get("client-order-id");
    }

    @Key("account-id")
    private String accountId;

    @Key("symbol")
    private String symbol;

    @Key("type")
    private String type;

    @Key("amount")
    private BigDecimal amount;

    @Key("price")
    private BigDecimal price;

    @Key("source")
    private String source;

    @Key("client-order-id")
    private String clientOrderId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public SpotOrder toSpotOrder() {
        SpotOrder spotOrder = new SpotOrder();


        // todo
        return spotOrder;
    }

}
