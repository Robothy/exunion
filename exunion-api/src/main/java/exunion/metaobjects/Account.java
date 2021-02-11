package exunion.metaobjects;

import java.math.BigDecimal;
import java.util.List;

public class Account extends Error {

    /**
     * 资产信息
     */
    private List<Account.Balance> balances = null;

    /**
     * 资产信息
     */
    public List<Account.Balance> getBalances() {
        return balances;
    }

    /**
     * 资产信息
     */
    public void setBalances(List<Account.Balance> balances) {
        this.balances = balances;
    }

    public static class Balance {

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
    }

}
