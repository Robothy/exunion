package exunion.exchange;

import java.math.BigDecimal;
import java.util.List;

import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Order;
import exunion.metaobjects.Ticker;

public class ExxExchange implements Exchange {

	public Account getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	public Depth getDepth(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	public Ticker getTicker(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Ticker> getAllTickers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Order getOrder(String currency, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Order> getOpenOrders(String currency, String side) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Order> getOpenOrders(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Order> getHistoryOrders(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	public Order order(String side, String currency, BigDecimal quantity, BigDecimal price) {
		// TODO Auto-generated method stub
		return null;
	}

	public Order cancel(String currency, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPlantformName() {
		// TODO Auto-generated method stub
		return null;
	}

}
