package com.robothy.exunion.huobi.market;

import com.robothy.exunion.core.meta.Symbol;

public class HuobiSymbol extends Symbol {

    public HuobiSymbol(String base, String quote) {
        super(base, quote);
    }

    /**
     * huobi symbol doesn't contains a separator between base String and quote String.
     * So we need to split it manually.
     * @param huobiSymbol huobi symbol in string format.
     */
    public HuobiSymbol(String huobiSymbol){
        super(null,null);
        int quoteLen = 2; // HT
        if (huobiSymbol.endsWith("eth") || huobiSymbol.endsWith("btc")) {
            quoteLen = 3;
        } else if (huobiSymbol.endsWith("usdt") || huobiSymbol.endsWith("husd") || huobiSymbol.endsWith("alts")) {
            quoteLen = 4;
        }
        super.setBase(huobiSymbol.substring(0, huobiSymbol.length() - quoteLen).toUpperCase());
        super.setQuote(huobiSymbol.substring(huobiSymbol.length() - quoteLen).toUpperCase());
    }

    @Override
    public String toString() {
        return (getBase() + getQuote()).toLowerCase();
    }

    public static Symbol of(String base, String quote) {
        return new HuobiSymbol(base, quote);
    }

    public static Symbol of(Symbol symbol) {
        return of(symbol.getBase(), symbol.getQuote());
    }

    public static Symbol of(String symbol){
        return new HuobiSymbol(symbol);
    }

}
