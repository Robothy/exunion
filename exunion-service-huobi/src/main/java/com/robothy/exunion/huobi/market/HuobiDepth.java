package com.robothy.exunion.huobi.market;

import com.google.api.client.util.Key;
import com.robothy.exunion.core.market.Depth;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class HuobiDepth {

    @Key("ch")
    private String ch;

    @Key("status")
    private String status;

    @Key("ts")
    private long ts;

    @Key("tick")
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

        @Key("bids")
        private List<BigDecimal[]> bids;

        @Key("asks")
        private List<BigDecimal[]> asks;

        @Key("version")
        private long version;

        @Key("ts")
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
    }
}