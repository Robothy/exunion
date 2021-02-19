package com.robothy.exunion.core.meta;

import java.util.Objects;

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

    public static Symbol of(Currency base, Currency quote){
        return new Symbol(base, quote);
    }

    public static Symbol of(String base, String quote){
        return new Symbol(Currency.valueOf(base), Currency.valueOf(quote));
    }

    @Override
    public String toString() {
        return String.format("%s/%s", base, quote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.base, this.quote);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Symbol)) return false;
        Symbol o = (Symbol) obj;
        return o == this || (o.base == this.base && o.quote == this.quote);
    }
}
