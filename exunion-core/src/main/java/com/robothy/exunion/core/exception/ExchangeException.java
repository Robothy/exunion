package com.robothy.exunion.core.exception;

import com.robothy.exunion.core.trade.TradingErrorMessage;

/**
 * Throws when API server report an error message for a request.
 * This exception should be handled manually.
 */
public class ExchangeException extends Exception {

    private TradingErrorMessage tradingErrorMessage;

    public ExchangeException(TradingErrorMessage tradingErrorMessage){
        super();
        this.tradingErrorMessage = tradingErrorMessage;
    }

    public TradingErrorMessage getTradingErrorMessage() {
        return tradingErrorMessage;
    }

    public void setTradingErrorMessage(TradingErrorMessage tradingErrorMessage) {
        this.tradingErrorMessage = tradingErrorMessage;
    }
}
