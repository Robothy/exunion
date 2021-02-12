package com.robothy.exunion.rest;

import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.spot.SpotTradingServiceBuilder;

/**
 * All trading service class must implement this interface.
 */
public interface ExchangeService {

    /**
     * @return the exchange that provide the trading service.
     */
    SupportedExchange exchange();

    /**
     * A hook invoked the service instance is created and exchange properties are set.
     * The service implementation could do properties verification and service initialization
     * by override this method.
     * <p>
     * This method will be invoked in the <code>build()</code> method of builder class.
     * <br>For example: {@link SpotTradingServiceBuilder#build()}
     */
    default void init() {}

}
