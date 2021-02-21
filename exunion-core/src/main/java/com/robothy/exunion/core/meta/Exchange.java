package com.robothy.exunion.core.meta;

/**
 * Exchange meta abstraction.
 */
public interface Exchange {

    /**
     * The exchange ID, it's an identifier of an exchange.
     */
    String id();

    /**
     * @return The default API server. This may not the best API server for a specific application.
     */
    String defaultApiServer();

    /**
     * @return The API references document URL that the implementation according to.
     */
    String apiRef();
}