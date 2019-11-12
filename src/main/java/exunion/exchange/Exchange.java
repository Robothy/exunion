package exunion.exchange;

import java.math.BigDecimal;
import java.util.List;

import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Order;
import exunion.metaobjects.Ticker;
import exunion.websocket.DeliveryBoy;

public interface Exchange {

	/**
	 * getAccount 获取账户信息
	 * @return 账户信息
	 * @see Account
	 */
	Account getAccount();
	
    /**
     * getDepth 获取交易深度
     * 
     * @param currency 交易币种
     * @return 市场深度
     * @see Depth
     */
    Depth getDepth(String currency);

    /**
     * getTicker 获取行情
     * 
     * @param currency 币种
     * @return
     */
    Ticker getTicker(String currency);
    
    /**
     * geAllTickers() 获取平台所有可交易的币种的行情信息
     * @return 所有币种的行情信息
     */
    List<Ticker> getAllTickers();
    
    /**
     * 
     * 
     * getOrder 根据order的部分信息获取订单的最新情况
     * @param symbol 币种对，例如 ETH_BTC, BTC_USDT 等
     * @param orderId 订单编号
     * @return 订单最新情况，当出现异常情况时返回 null
     */
    Order getOrder(String symbol, String orderId);
    
    /**
     * getOrders 获取委托的挂单
     * 
     * @param symbol 币种对，例如 ETH_BTC, BTC_USDT 等
     * @param side 类型 BUY/SELL
     * @return 委托的挂单，当出现异常情况时返回 null
     */
    List<Order> getOpenOrders(String symbol, String side);
    
    /**
     * getOrders 获取委托的挂单
     * @param currency 币种
     * @return 获取币种 currency 下所有正在进行的委托挂单，当出现异常情况时返回 null
     */
    List<Order> getOpenOrders(String currency);
    
    /**
     * getHistoryOrsers 获取用户的历史订单
     * @param symbol 币种对，例如 ETH_BTC, BTC_USDT 等
     * @return 历史已成交的订单列表
     */
    List<Order> getHistoryOrders(String symbol);
    
    /**
     * 下单操作
     * @param side 下单方向, 买入/卖出
     * @param symbol 币种对，例如 ETH_BTC, BTC_USDT 等
     * @param quantity 数量,当带有小数时在创建 BigDecimal 对象时请使用字符串类型
     * <p>例如：
     * <p> <code>BigDecimal quantity = new BigDecimal("2.33");</code>
     * @param price 价格, 当带有小数时在创建 BigDecimal 对象时请使用字符串类型
     * <p>例如：
     * <p> <code>BigDecimal price = new BigDecimal("2.33");</code>
     * @return 订单信息
     * <p><b>注意</b>：返回的订单信息中应包含订单编号、币种。其它属性与实际执行结果可能不一致，要获取最新的执行结果应该使用<code>getOrder</code>方法获取。
     * 
     */
    Order order(String side, String symbol, BigDecimal quantity, BigDecimal price);
    
    /**
     * 取消操作
     * @param symbol 币种对，例如 ETH_BTC, BTC_USDT 等
     * @param orderId 订单ID
     * @return 取消之后的订单信息
     */
    Order cancel(String symbol, String orderId);

    /**
     * 订阅深度信息
     * @param symbol 币种对，例如 ETH_BTC, BTC_USDT 等
     */
    DeliveryBoy<Depth> subscribeDepth(String symbol);

    /**
     * 订阅行情信息
     */
    DeliveryBoy<Ticker> subscribeTicker(String symbol);

    /**
     * 获取平台名称
     * @return 平台名称
     */
    String getExchangeName();
}