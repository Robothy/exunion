package com.robothy.exunion.rest;

import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.rest.spi.Options;

/**
 * All trading service class must implement this interface.
 */
public interface ExchangeService {

    /**
     * @return the exchange that provide the trading service.
     */
    Exchange exchange();

    /**
     * A hook invoked the service instance is created and exchange properties are set.
     * The service implementation could do properties verification and service initialization
     * by override this method.
     */
    default void init(Options options) {
        // empty implementation for those who needn't override this method.
    }

}
