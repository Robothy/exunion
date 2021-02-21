package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.rest.Result;

import java.io.IOException;
import java.util.List;

public interface SpotOrderCreateService {

    /**
     * Create a spot order on a specific exchange.
     * @param spotOrder required spot order options.
     * @return the operation result.
     */
    Result<SpotOrderDetails> create(SpotOrder spotOrder) throws IOException;

    /**
     * Create a batch of spot order.
     * @param spotOrders spot order list.
     * @return the operation result for each spot order.
     */
    List<Result<SpotOrderDetails>> create(List<SpotOrder> spotOrders) throws IOException;

}
