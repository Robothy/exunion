package com.robothy.exunion.binance.market;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.OrderBook;
import com.binance.api.client.exception.BinanceApiException;
import com.robothy.exunion.binance.Binance;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.ExchangeService;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.spi.Options;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BinanceDepthService implements DepthService, ExchangeService {

    private BinanceApiRestClient client;

    private static final int[] validDepth = new int[]{5, 10, 20, 50, 100, 500, 1000, 5000};

    @Override
    public void init(Options options) {
        this.client = BinanceApiClientFactory.newInstance().newRestClient();
    }

    @Override
    public Exchange exchange() {
        return Binance.BINANCE;
    }

    @Override
    public Result<Depth> getDepth(Symbol symbol) throws IOException {
        return getDepth(symbol, 50);
    }

    @Override
    public Result<Depth> getDepth(Symbol symbol, int originLimit) throws IOException {
        try {
            if (originLimit <= 0) {
                throw new IllegalArgumentException("The depth limit must be a positive integer.");
            }
            int limit;
            int idx = Arrays.binarySearch(validDepth, originLimit);
            limit = idx < 0 ? validDepth[Math.max(-idx - 1, validDepth.length - 1)] : validDepth[idx];
            OrderBook orderBook = client.getOrderBook(symbol.getBase() + symbol.getQuote(), limit);
            Depth depth = new Depth();
            depth.setAsks(orderBook.getAsks().stream().map(entry -> new Depth.PriceQuotation(new BigDecimal(entry.getPrice()), new BigDecimal(entry.getQty()))).collect(Collectors.toList()));
            depth.setBids(orderBook.getBids().stream().map(entry -> new Depth.PriceQuotation(new BigDecimal(entry.getPrice()), new BigDecimal(entry.getQty()))).collect(Collectors.toList()));
            depth.setSymbol(symbol);
            return new Result<>(depth, orderBook);
        } catch (BinanceApiException e) {
            if (e.getError() != null) {
                return new Result<>(String.valueOf(e.getError().getCode()), e.getError().getMsg());
            }
            throw new IOException(e.getCause());
        }
    }

}
