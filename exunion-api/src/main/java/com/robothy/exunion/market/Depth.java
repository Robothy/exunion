package com.robothy.exunion.market;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Depth {

    /** 投标价格（买价） */
    private List<PriceQuotation> bids = null;

    /**
     * 投标价格（买价）
     */
    public List<PriceQuotation> getBids() {
        return bids;
    }

    /**
     * set the bids, make sure the are sorted by price in <b>ascending</b>.
     * <p>
     * <code>
     *     Arrays.sort(bids, Depth.PriceQuotation.ASC);
     * <code/>
     */
    public void setBids(List<PriceQuotation> bids) {
        this.bids = bids;
    }

    /**
     * 报价（卖价）
     */
    private List<PriceQuotation> asks = null;

    /**
     * 返回交易平台排序之后的卖单
     * @return 有序的卖单
     */
    public List<PriceQuotation> getAsks() {
        return asks;
    }

    /**
     * Set the asks. Make sure the passed parameter is sorted by price in <b>decreasing</b>.
     * <p>
     * <code>
     *     Arrays.sort(asks, Depth.PriceQuotation.DESC);
     * </code>
     */
    public void setAsks(List<PriceQuotation> asks) {
        this.asks = asks;
    }

    private String symbol = null;

    public String getSymbol() {
        return symbol;
    }

    /**
     * format: {base currency}_{quote currency}
     * @param symbol ETH_BTC, BTC_USDT, ...
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * 时间戳
     */
    private Long timestamp = null;

    /**
     * 时间戳
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * timestamp returned from API.
     * 时间戳
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * price and quantity pair
     * @author robothy
     *
     */
    public static class PriceQuotation{

        public static final Comparator<PriceQuotation> ASC = Comparator.comparing(a -> a.price);

        public static final Comparator<PriceQuotation> DESC = (a, b) -> b.price.compareTo(a.price);

        public PriceQuotation(BigDecimal price, BigDecimal quantity){
            this.price = price;
            this.quantity = quantity;
        }

        /**
         * 价格
         */
        private BigDecimal price;

        /**
         * 价格
         */
        public BigDecimal getPrice() {
            return price;
        }

        /**
         * 价格
         */
        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        /**
         * 数量
         */
        private BigDecimal quantity = null;

        /**
         * set 数量
         */
        public BigDecimal getQuantity() {
            return quantity;
        }

        /**
         * get 数量
         */
        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

    }
}
