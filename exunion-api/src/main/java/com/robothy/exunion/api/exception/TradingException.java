package com.robothy.exunion.api.exception;

import com.robothy.exunion.api.trade.TradingErrorMessage;

/**
 * Throws when API server report an error message for a request.
 * This exception should be handled manually.
 */
public class TradingException extends Exception {

    private TradingErrorMessage tradingErrorMessage;

    public TradingException(TradingErrorMessage tradingErrorMessage){
        super();
        this.tradingErrorMessage = tradingErrorMessage;
    }

}
