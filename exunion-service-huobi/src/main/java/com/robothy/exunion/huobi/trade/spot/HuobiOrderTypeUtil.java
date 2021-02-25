package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.trade.spot.SpotOrder;

public class HuobiOrderTypeUtil {

    public static SpotOrder.Type getType(String huobiType) {
        return huobiType.contains("market") ? SpotOrder.Type.MARKET : SpotOrder.Type.LIMIT;
    }

    public static SpotOrder.Side getSide(String huobiType) {
        return huobiType.contains("buy") ? SpotOrder.Side.BUY : SpotOrder.Side.SELL;
    }

    public static String of(SpotOrder.Side side, SpotOrder.Type type) {
        return side.name().toLowerCase() + "-" + type.name().toLowerCase();
    }
}
