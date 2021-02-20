package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.trade.spot.SpotOrderDetails;

public class HuobiSpotOrderDetail extends HuobiSpotOrder {


    public SpotOrderDetails toSpotOrderDetail(){
        SpotOrderDetails spotOrderDetails = new SpotOrderDetails();
        return spotOrderDetails;
    }

}
