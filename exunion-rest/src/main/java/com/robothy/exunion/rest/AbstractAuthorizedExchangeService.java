package com.robothy.exunion.rest;

import com.robothy.exunion.rest.spi.Options;

import java.util.Objects;

/**
 * Check and initialize the options that used by the exchange services that requires authorization.
 */
public abstract class AbstractAuthorizedExchangeService extends AbstractExchangeService {
    @Override
    public void init(Options options) {
        super.init(options);
        Objects.requireNonNull(options.getToken(), "The token is required to visit authorized service of " + exchange().id() + " exchange.");
    }
}
