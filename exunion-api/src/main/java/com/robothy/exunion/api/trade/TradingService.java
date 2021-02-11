package com.robothy.exunion.api.trade;

import com.robothy.exunion.api.meta.SupportedExchange;

/**
 * All trading service class must implement this interface.
 */
public interface TradingService {

    /**
     * @return the exchange that provide the trading service.
     */
    SupportedExchange exchange();

}
