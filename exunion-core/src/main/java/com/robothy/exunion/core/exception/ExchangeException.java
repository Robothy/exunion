package com.robothy.exunion.core.exception;

import com.robothy.exunion.core.trade.ExchangeErrorMessage;

/**
 * Throws when API server report an error message for a request.
 * This exception should be handled manually.
 */
public class ExchangeException extends Exception {

    private ExchangeErrorMessage exchangeErrorMessage;

    public ExchangeException(ExchangeErrorMessage exchangeErrorMessage){
        this.exchangeErrorMessage = exchangeErrorMessage;
    }

    public ExchangeException(String message){
        super(message);
    }

    public ExchangeErrorMessage getTradingErrorMessage() {
        return exchangeErrorMessage;
    }

    public void setTradingErrorMessage(ExchangeErrorMessage exchangeErrorMessage) {
        this.exchangeErrorMessage = exchangeErrorMessage;
    }
}
