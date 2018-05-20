package exunion.metaobjects;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Depth extends Error {
	
    /**
	 * 投标价格（买价）
	 */
	private List<PriceQuotation> bids = null;

	/**
	 * 投标价格（买价）
	 */
	public List<PriceQuotation> getBids() {
		return bids;
	}

	/**
	 * 投标价格（买价）
	 */
	public void setBids(List<PriceQuotation> bids) {
		this.bids = bids;
		Collections.sort(this.bids, new PriceQuotation().getComparator());
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
	 * <b>卖价</b>
	 * <p>由于某些平台返回的卖单或卖单数据有可能没有按照价格排序，
	 * 此方法会对传入的 ask 按照价格从低到高排序。
	 * <p>通过 <code>getAsks().get(0)</code>获取到的为价格最低的卖单，即卖一。
	 * @param asks 从平台获取到的卖单数据，可能无序。
	 */
	public void setAsks(List<PriceQuotation> asks) {
		this.asks = asks;
		Collections.sort(this.asks, new PriceQuotation().getComparator());
		Collections.reverse(this.asks);
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
	 * 标准化的币种，如 ETH_BTC, BTC_USDT, ...
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * 交易所名称
	 */
	private String exchange = null;
	
	/**
	 * 获取交易所名称
	 * @return 交易所名称
	 */
	public String getExchange() {
		return exchange;
	}

	/**
	 * 设置交易所名称
	 * @param exchange 交易锁名称
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
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
	 * 时间戳
	 */
	public void setTimestamp(Long timestamp) {
	    this.timestamp = timestamp;
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("\r\n");
		result.append(exchange);
		result.append(" ");
		result.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp * 1000)));
		result.append(" ");
		result.append(currency);
		result.append("\r\n|");
		result.append("\r\n|--");
		result.append("asks: [");
		
		asks.stream().forEach(p->result.append(p).append(", "));
		result.deleteCharAt(result.length() - 2);
		result.append("]\r\n|--bids: [");
		bids.stream().forEach(e->result.append(e).append(", "));
		
		result.deleteCharAt(result.length() - 2);
		result.append("]");
		return result.toString();
	}

	/**
	 * 报价
	 * @author robothy
	 *
	 */
	public static class PriceQuotation{
	    
		private Comparator<PriceQuotation> comparator = new Comparator<Depth.PriceQuotation>() {
			public int compare(PriceQuotation o1, PriceQuotation o2) {
				return o1.getPrice().compareTo(o1.getPrice());
			}
		};
		
	    public Comparator<PriceQuotation> getComparator() {
			return comparator;
		}

		public void setComparator(Comparator<PriceQuotation> comparator) {
			this.comparator = comparator;
		}

		public PriceQuotation(BigDecimal price, BigDecimal quantity){
	        this.price = price;
	        this.quantity = quantity;
	    }
	    	
	    public PriceQuotation(){}
	    
		/**
		 * 价格
		 */
		private BigDecimal price = null;
		
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
		 * 数量
		 */
		public BigDecimal getQuantity() {
			return quantity;
		}

		/**
		 * 数量
		 */
		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}
		
		public String toString(){
			return "(" + this.price + ", " + this.quantity + ")";
		}
		
	}	
}
