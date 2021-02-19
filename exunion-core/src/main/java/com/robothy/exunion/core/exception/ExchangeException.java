package com.robothy.exunion.core.exception;

import com.robothy.exunion.core.trade.ExchangeErrorMessage;

/**
 * Throws when API server report an error message for a request.
 * This exception should be handled manually.
 */
public class ExchangeException extends Exception {

    private ExchangeErrorMessage exchangeErrorMessage;

    public ExchangeException(ExchangeErrorMessage exchangeErrorMessage){
        super(exchangeErrorMessage.message());
        this.exchangeErrorMessage = exchangeErrorMessage;
    }

    public ExchangeException(String message){
        super(message);
    }

    public ExchangeErrorMessage getExchangeErrorMessage() {
        return exchangeErrorMessage;
    }
}
