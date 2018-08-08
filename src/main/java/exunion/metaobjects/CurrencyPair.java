package exunion.metaobjects;

import java.math.BigDecimal;

/**
 * 交易币种对
 */
public class CurrencyPair {

    /**
     * 带币种对的构造参数
     * @param currencyPair 交易币种对
     */
    public CurrencyPair(String currencyPair){
        this.currencyPair = currencyPair;
        String[] currArr = currencyPair.split("_");
        baseCurrency = currArr[0];
        quoteCurrency = currArr[1];
    }

    /**
     * 构造方法
     * @param currencyPair 币种对
     * @param priceScale 价格精度
     * @param quantityScale 量精度
     */
    public CurrencyPair(String currencyPair, Integer priceScale, Integer quantityScale){
        this(currencyPair);
        this.priceScale = priceScale;
        this.quantityScale = quantityScale;
    }

    /**
     * 构造方法
     * @param baseCurrency 基础货币
     * @param quoteCurrency 计价货币
     * @param priceScale 价格精度
     * @param quantityScale 量精度
     */
    public CurrencyPair(String baseCurrency, String quoteCurrency, Integer priceScale, Integer quantityScale){
        this(baseCurrency, quoteCurrency);
        this.priceScale = priceScale;
        this.quantityScale = quantityScale;
    }

    /**
     * 带基础币和计价币的构造参数
     * @param baseCurrency 基础货币
     * @param quoteCurrency 计价货币
     */
    public CurrencyPair(String baseCurrency, String quoteCurrency){
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
    }

    /**
     * 交易所
     */
    private String exchangeName;

    /**
     * 获取交易所
     */
    public String getExchangeName() {
        return exchangeName;
    }

    /**
     * 设置交易所
     */
    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    /**
     * 计价货币
     */
    private String quoteCurrency;

    /**
     * 获取计价货币
     * @return 计价货币
     */
    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    /**
     * 设置计价货币
     * @param quoteCurrency 计价货币
     */
    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    /**
     * 币种对
     */
    private String currencyPair;

    /**
     * 获取由 "基础币_计价币" 拼接成的交易币种对
     * @return 交易币种对
     */
    public String getCurrencyPair() {
        if(null == this.currencyPair){
            this.currencyPair = baseCurrency + "_" + quoteCurrency;
        }
        return currencyPair;
    }

    /**
     * 基础货币
     */
    private String baseCurrency;

    /**
     * 获取基础货币
     * @return 基础货币
     */
    public String getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * 基础货币
     * @param baseCurrency 基础货币
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    /**
     * 价格精度
     */
    private Integer priceScale;

    /**
     * 获取价格进度
     * @return 价格精度
     */
    public Integer getPriceScale() {
        return priceScale;
    }

    /**
     * 获取价格精度
     * @param priceScale 价格精度
     */
    public void setPriceScale(Integer priceScale) {
        this.priceScale = priceScale;
    }

    /**
     * 量精度
     */
    private Integer quantityScale;

    /**
     * 获取量精度
     * @return 量精度
     */
    public Integer getQuantityScale() {
        return quantityScale;
    }

    /**
     * 设置量精度
     * @param quantityScale 量精度
     */
    public void setQuantityScale(Integer quantityScale) {
        this.quantityScale = quantityScale;
    }

    /**
     * 买入费率
     */
    private BigDecimal buyFeeRate;

    /**
     * 获取买入费率
     * @return 买入费率
     */
    public BigDecimal getBuyFeeRate() {
        return buyFeeRate;
    }

    /**
     * 设置买入费率
     * @param buyFeeRate 买入费率
     */
    public void setBuyFeeRate(BigDecimal buyFeeRate) {
        this.buyFeeRate = buyFeeRate;
    }

    /**
     * 卖出费率
     */
    private BigDecimal sellFeeRate;

    /**
     * 获取卖出费率
     * @return 卖出费率
     */
    public BigDecimal getSellFeeRate() {
        return sellFeeRate;
    }

    /**
     * 设置卖出费率
     * @param sellFeeRate 卖出费率
     */
    public void setSellFeeRate(BigDecimal sellFeeRate) {
        this.sellFeeRate = sellFeeRate;
    }
}
