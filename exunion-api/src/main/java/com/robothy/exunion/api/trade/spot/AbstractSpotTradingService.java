package com.robothy.exunion.api.trade.spot;

import java.math.BigDecimal;

/**
 * Common properties of spot trading service.
 */
public class AbstractSpotTradingService {

    /**
     * The fee rate when you buy crypto currency, this property has a higher priority than <code>defaultFeeRate</code>
     * when determine the actual fee rate.
     * <p>
     * What is buy-in fee rate? Buy-in fee rate is the fee rate that a buyer paid for a transaction.
     * For example, you have an buy-in order to buy 1000 BTC in huobi.com, and the buy-in fee rate in
     * huobi.com is 0.002; when this order executed completely, you actually got 998 BTC, and the other
     * 1000 * 0.002 = 2 BTC paid to huobi.com as the transaction fee.
     */
    private BigDecimal defaultBuyInFeeRate;

    /**
     * Similar with <code>defaultSellOutFeeRate</code>. Sell out fee is the charge paid
     * from seller to exchange.
     */
    private BigDecimal defaultSellOutFeeRate;

    private BigDecimal defaultFeeRate;

    public BigDecimal getDefaultBuyInFeeRate() {
        return defaultBuyInFeeRate;
    }

    public void setDefaultBuyInFeeRate(BigDecimal defaultBuyInFeeRate) {
        this.defaultBuyInFeeRate = defaultBuyInFeeRate;
    }

    public BigDecimal getDefaultSellOutFeeRate() {
        return defaultSellOutFeeRate;
    }

    public void setDefaultSellOutFeeRate(BigDecimal defaultSellOutFeeRate) {
        this.defaultSellOutFeeRate = defaultSellOutFeeRate;
    }

    public BigDecimal getDefaultFeeRate() {
        return defaultFeeRate;
    }

    public void setDefaultFeeRate(BigDecimal defaultFeeRate) {
        this.defaultFeeRate = defaultFeeRate;
    }
}
