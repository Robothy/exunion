package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.rest.Result;

import java.io.IOException;

public interface SpotOrderCancelService {

    <T> T cancel(SpotOrderDetails spotOrderDetails, Class<T> model) throws IOException;

    Result<SpotOrderDetails> cancel(SpotOrderDetails spotOrderDetails);

}