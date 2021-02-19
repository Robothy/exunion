package com.robothy.exunion.huobi.market;

import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.Symbol;

public class HuobiSymbol extends Symbol {

    public HuobiSymbol(Currency base, Currency quote) {
        super(base, quote);
    }

    @Override
    public String toString() {
        return (getBase().toString() + getQuote().toString()).toLowerCase();
    }

    public static Symbol of(Currency base, Currency quote){
        return new HuobiSymbol(base, quote);
    }

    public static Symbol of(Symbol symbol){
        return symbol == null ? null : of(symbol.getBase(), symbol.getQuote());
    }

}
