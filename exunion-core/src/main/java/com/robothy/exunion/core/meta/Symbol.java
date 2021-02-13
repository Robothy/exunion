package com.robothy.exunion.core.meta;

public class Symbol {

    private Currency base;

    private Currency quote;

    public Symbol(Currency base, Currency quote) {
        this.base = base;
        this.quote = quote;
    }

    public Currency getBase() {
        return base;
    }

    public void setBase(Currency base) {
        this.base = base;
    }

    public Currency getQuote() {
        return quote;
    }

    public void setQuote(Currency quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return String.format("%s/%s", base, quote);
    }
}
