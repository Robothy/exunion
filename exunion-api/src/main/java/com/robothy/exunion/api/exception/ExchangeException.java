package com.robothy.exunion.api.exception;

import com.robothy.exunion.api.trade.ExchangeErrorMessage;

/**
 * Throws when API server report an error message for a request.
 * This exception should be handled manually.
 */
public class ExchangeException extends Exception {

    private ExchangeErrorMessage exchangeErrorMessage;

    public ExchangeException(ExchangeErrorMessage exchangeErrorMessage){
        super();
        this.exchangeErrorMessage = exchangeErrorMessage;
    }

}
