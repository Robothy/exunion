package com.robothy.exunion.core.meta;

import java.util.Objects;

public class Symbol {

    private String base;

    private String quote;

    public Symbol(String base, String quote) {
        this.base = base;
        this.quote = quote;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public static Symbol of(String base, String quote){
        return new Symbol(base, quote);
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
        return o == this || (o.base.equals(this.base) && o.quote.equals(this.quote));
    }
}
