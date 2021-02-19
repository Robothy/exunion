package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.trade.spot.SpotOrder;

public class HuobiOrderType {

    public static String of(SpotOrder.Side side, SpotOrder.Type type){
        return side.name().toLowerCase() + type.name().toLowerCase();
    }

}
