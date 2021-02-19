package com.robothy.exunion.huobi.market;

import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HuobiSymbolTest {

    @Test
    void testToString() {
        Symbol symbol = HuobiSymbol.of(Symbol.of(Currency.ETH, Currency.USDT));
        Assertions.assertEquals("ethusdt", symbol.toString());
    }

    @Test
    void of() {
        Symbol symbol = HuobiSymbol.of(Currency.LTC, Currency.BTC);
        Assertions.assertEquals(Currency.LTC, symbol.getBase());
        Assertions.assertEquals(Currency.BTC, symbol.getQuote());
    }

    @Test
    void of1(){
        Symbol symbol = HuobiSymbol.of(new Symbol(Currency.ETH, Currency.BTC));
        Assertions.assertEquals(Currency.ETH, symbol.getBase());
        Assertions.assertEquals(Currency.BTC, symbol.getQuote());
        Assertions.assertEquals("ethbtc", symbol.toString());
    }

    @Test
    void testOf() {
        Symbol symbol = HuobiSymbol.of("btcusdt");
        Assertions.assertEquals(Currency.BTC, symbol.getBase());
        Assertions.assertEquals(Currency.USDT, symbol.getQuote());

        symbol = HuobiSymbol.of("ltcbtc");
        Assertions.assertEquals(Currency.LTC, symbol.getBase());
        Assertions.assertEquals(Currency.BTC, symbol.getQuote());

        symbol = HuobiSymbol.of("btcht");
        Assertions.assertEquals(Currency.BTC, symbol.getBase());
        Assertions.assertEquals(Currency.HT, symbol.getQuote());
    }
}