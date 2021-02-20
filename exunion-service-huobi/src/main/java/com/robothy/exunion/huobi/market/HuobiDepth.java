package com.robothy.exunion.huobi.market;

import com.google.api.client.util.Key;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.huobi.common.HuobiResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class HuobiDepth extends HuobiResponse {

    @Key(Keys.CH)
    private String ch;

    @Key(Keys.TS)
    private long ts;

    @Key(Keys.TICK)
    private Tick tick;

    public Depth toDepth() {
        Depth depth = new Depth();
        depth.setAsks(toStandardTicks(tick.getAsks()));
        depth.setBids(toStandardTicks(tick.getBids()));
        depth.setTimestamp(tick.getTs());
        return depth;
    }

    private List<Depth.PriceQuotation> toStandardTicks(List<BigDecimal[]> tick){
        return tick.stream().map(t -> new Depth.PriceQuotation(t[0], t[1])).collect(Collectors.toList());
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public Tick getTick() {
        return tick;
    }

    public void setTick(Tick tick) {
        this.tick = tick;
    }

    public static class Tick {

        @Key(Keys.BIDS)
        private List<BigDecimal[]> bids;

        @Key(Keys.ASKS)
        private List<BigDecimal[]> asks;

        @Key(Keys.VERSION)
        private long version;

        @Key(Keys.TS)
        private long ts;

        public List<BigDecimal[]> getBids() {
            return bids;
        }

        public void setBids(List<BigDecimal[]> bids) {
            this.bids = bids;
        }

        public List<BigDecimal[]> getAsks() {
            return asks;
        }

        public void setAsks(List<BigDecimal[]> asks) {
            this.asks = asks;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public interface Keys {
            String BIDS = "bids";
            String ASKS = "asks";
            String VERSION = "version";
            String TS = "ts";
        }
    }

    public interface Keys{
        String CH = "ch";
        String TS = "ts";
        String TICK = "tick";
    }
}