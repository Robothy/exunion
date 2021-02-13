package com.robothy.exunion.rest.market;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.ExchangeService;

import java.io.IOException;

public interface DepthService extends ExchangeService {

    /**
     * Get depth by symbol.
     *
     * @param symbol the trading pair, for example ETH_BTC, BTC_USDT, etc.
     * @return the asks and bids of related symbol.
     * @throws ExchangeException when the exchange API server returns error message.
     * @throws IOException when network reports error.
     */
    Depth getDepth(Symbol symbol) throws ExchangeException, IOException;

    /**
     * Get the depth by symbol.
     *
     * @param symbol the crypto currency pair.
     * @param depth the max length of bids and asks. <b>Note: If there are not enough bids or asks,
     * the actual returned depth may smaller than the passed <code>depth</code> parameter.</b>
     */
    Depth getDepth(Symbol symbol, int depth) throws ExchangeException, IOException;

}
