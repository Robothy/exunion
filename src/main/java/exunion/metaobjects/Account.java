package exunion.metaobjects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Account extends Error {

	public Account(){
		this.balances = new HashMap<>();
	}
	
	/**
	 * 资产信息
	 */
	private Map<String, Account.Balance> balances = null;
	
	/**
	 * 资产信息
	 */
	public Map<String, Account.Balance> getBalances() {
		return balances;
	}

	/**
	 * 资产信息
	 */
	public void setBalances(Map<String, Account.Balance> balances) {
		this.balances = balances;
	}
	
	/**
	 * 获取特定资产，若资产不存在，则返回一个可用额为0，锁定额为0的Balance对象
	 */
	public Balance getBalance(String currency){
		Balance balance = balances.get(currency);
		if(null == balance){
			balance = new Balance();
			balance.setAsset(currency);
			balance.setFree(BigDecimal.ZERO);
			balance.setLocked(BigDecimal.ZERO);
		}
		return balance;
	}
	
	/**
	 * 设置特定资产
	 * @param balance
	 */
	public void putBalance(Balance balance){
		balances.put(balance.getAsset(), balance);
	}

	public static class Balance{
		
		/**
		 * 资产 BTC, ETH, HSR ...
		 */
		private String asset = null;
		
		/**
		 * 资产 BTC, ETH, HSR ...
		 */
		public String getAsset() {
			return asset;
		}

		/**
		 * 资产 BTC, ETH, HSR ...
		 */
		public void setAsset(String asset) {
			this.asset = asset;
		}

		/**
		 * 可用资产
		 */
		private BigDecimal free = null;
		
		/**
		 * 可用资产
		 */
		public BigDecimal getFree() {
			return free;
		}

		/**
		 * 可用资产
		 */
		public void setFree(BigDecimal free) {
			this.free = free;
		}

		/**
		 * 冻结资产
		 */
		private BigDecimal locked = null;

		/**
		 * 冻结资产
		 */
		public BigDecimal getLocked() {
			return locked;
		}

		/**
		 * 冻结资产
		 */
		public void setLocked(BigDecimal locked) {
			this.locked = locked;
		}
		
		public String toString(){
			return "(currency=" + asset + ", free:" + free.stripTrailingZeros().toPlainString() + ", lock=" + locked.stripTrailingZeros().toPlainString() + ")";
		}
		
	}
}
