package exunion.metaobjects;

import java.math.BigDecimal;

public class Order extends Error {

	/**
	 * 订单状态
	 */
	private String status = null;
	
	/**
	 * 订单状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 订单状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 订单类型
	 */
	private String type = null;
	
	/**
	 * 订单类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 订单类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 订单方向
	 */
	private String side = null;

	/**
	 * 订单方向
	 */
	public String getSide() {
		return side;
	}

	/**
	 * 订单方向
	 */
	public void setSide(String side) {
		this.side = side;
	}
	
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
	 * 挂单数量
	 */
	private BigDecimal quantity = null;
	
	/**
	 * 挂单数量
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * 挂单数量
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * 挂单价格
	 */
	private BigDecimal price = null;
	
	/**
	 * 挂单价格
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 挂单价格
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	/**
	 * 已成交金额
	 */
	private BigDecimal tradeMoney;
	
	/**
	 * 已成交金额
	 */
	public BigDecimal getTradeMoney() {
		return tradeMoney;
	}

	/**
	 * 已成交金额
	 */
	public void setTradeMoney(BigDecimal tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	/**
	 * 订单编号
	 */
	private String orderId = null;

	/**
	 * 订单编号
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * 订单编号
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * 已成交数量
	 */
	private BigDecimal tradeQuantity;
	
	/**
	 * 已成交数量
	 */
	public BigDecimal getTradeQuantity() {
		return tradeQuantity;
	}

	/**
	 * 已成交数量
	 */
	public void setTradeQuantity(BigDecimal tradeQuantity) {
		this.tradeQuantity = tradeQuantity;
	}
	
	/**
	 * 委托时间
	 */
	private Long createDate;

	/**
	 * 委托时间
	 */
	public Long getCreateDate() {
		return createDate;
	}

	/**
	 * 委托时间
	 */
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	
}
