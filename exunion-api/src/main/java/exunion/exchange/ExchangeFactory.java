package exunion.exchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExchangeFactory {

    private static final Logger LOGGER = LogManager.getLogger(ExchangeFactory.class);

    /**
     * 根据平台名称获取一个交易实例
     *
     * @param plantform 平台名称，例如 exx.com, binance.com
     * @return 某个平台的实例
     */
    public static Exchange newInstance(String plantform) {
        if (plantform.equals("exx.com")) {
            return new ExxExchange();
        } else if (plantform.equals("binance.com")) {
            return new BinanceExchange();
        } else if (plantform.equals("zb.com")) {
            return new ZbExchange();
        } else {
            LOGGER.error("未找到 " + plantform + " 的交易实例。");
            return null;
        }

    }
}
