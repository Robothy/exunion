package exunion.metaobjects;

import java.math.BigDecimal;

/**
 * 报价
 *
 * @author robothy
 */
public class Ticker extends Error {

    /**
     * 币种
     */
    private String currency = null;

    /**
     * 币种
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 币种
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 24小时涨跌幅
     */
    private BigDecimal riseRate = null;

    /**
     * 24小时涨跌幅
     */
    public BigDecimal getRiseRate() {
        return riseRate;
    }

    /**
     * 24小时涨跌幅
     */
    public void setRiseRate(BigDecimal riseRate) {
        this.riseRate = riseRate;
    }

    /**
     * 最新成交价
     */
    private BigDecimal lastPrice = null;

    /**
     * 最新成交价
     */
    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    /**
     * 最新成交价
     */
    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    /**
     * 最新成交量
     */
    private BigDecimal lastQuantity = null;

    /**
     * 最新成交量
     */
    public BigDecimal getLastQuantity() {
        return lastQuantity;
    }

    /**
     * 最新成交量
     */
    public void setLastQuantity(BigDecimal lastQuantity) {
        this.lastQuantity = lastQuantity;
    }

    /**
     * 买一价(报价)
     */
    private BigDecimal bidPrice = null;

    /**
     * 买一价(报价)
     */
    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    /**
     * 买一价(报价)
     */
    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    /**
     * 卖一价（标价）
     */
    private BigDecimal askPrice = null;

    /**
     * 卖一价（标价）
     */
    public BigDecimal getAskPrice() {
        return askPrice;
    }

    /**
     * 卖一价（标价）
     */
    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    /**
     * 开盘价
     */
    private BigDecimal openingPrice = null;

    /**
     * 开盘价
     */
    public BigDecimal getOpeningPrice() {
        return openingPrice;
    }

    /**
     * 开盘价
     */
    public void setOpeningPrice(BigDecimal openingPrice) {
        this.openingPrice = openingPrice;
    }

    /**
     * 最高价
     */
    private BigDecimal highPrice = null;

    /**
     * 最高价
     */
    public BigDecimal getHighPrice() {
        return highPrice;
    }

    /**
     * 最高价
     */
    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    /**
     * 最低价
     */
    private BigDecimal lowPrice = null;

    /**
     * 最低价
     */
    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    /**
     * 最低价
     */
    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    /**
     * 24小时成交量
     */
    private BigDecimal volume = null;

    /**
     * 24小时成交量
     */
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * 24小时成交量
     */
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * 开始时间
     */
    private Long openTime = null;

    /**
     * 开始时间
     */
    public Long getOpenTime() {
        return openTime;
    }

    /**
     * 开始时间
     */
    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    /**
     * 结束时间，与开始时间相差24小时
     */
    private Long closeTime = null;

    /**
     * 结束时间，与开始时间相差24小时
     */
    public Long getCloseTime() {
        return closeTime;
    }

    /**
     * 结束时间，与开始时间相差24小时
     */
    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * 开始交易ID
     */
    private String firstId = null;

    /**
     * 开始交易ID
     */
    public String getFirstId() {
        return firstId;
    }

    /**
     * 开始交易ID
     */
    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    /**
     * 最后交易ID
     */
    private String lastId = null;

    /**
     * 最后交易ID
     */
    public String getLastId() {
        return lastId;
    }

    /**
     * 最后交易ID
     */
    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    /**
     * 交易笔数
     */
    private Long count = null;

    /**
     * 交易笔数
     */
    public Long getCount() {
        return count;
    }

    /**
     * 交易笔数
     */
    public void setCount(Long count) {
        this.count = count;
    }
}
