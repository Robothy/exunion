package com.robothy.exunion.huobi.market;

import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.Symbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HuobiSymbolTest {

    @Test
    void testToString() {
        Symbol symbol = HuobiSymbol.of(Symbol.of(Currency.ETH, Currency.USDT));
        assertNotNull(symbol);
        assertEquals("ethusdt", symbol.toString());
    }

    @Test
    void of() {
        Symbol symbol = HuobiSymbol.of(Currency.LTC, Currency.BTC);
        assertNotNull(symbol);
        assertEquals(Currency.LTC, symbol.getBase());
        assertEquals(Currency.BTC, symbol.getQuote());
    }

    @Test
    void of1(){
        Symbol symbol = HuobiSymbol.of(new Symbol(Currency.ETH, Currency.BTC));
        assertNotNull(symbol);
        assertEquals(Currency.ETH, symbol.getBase());
        assertEquals(Currency.BTC, symbol.getQuote());
        assertEquals("ethbtc", symbol.toString());
    }

    @CsvSource({
            "btcusdt, BTC, USDT",
            "ltcbtc, LTC, BTC",
            "btcht, BTC, HT"
    })
    @ParameterizedTest
    void testOf(String huobiSymbol, String base, String quote) {
        Symbol symbol = HuobiSymbol.of(huobiSymbol);
        assertNotNull(symbol);
        assertEquals(base, symbol.getBase());
        assertEquals(quote, symbol.getQuote());
    }
}