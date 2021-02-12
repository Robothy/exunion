package com.robothy.exunion.api.market;

interface DepthService {

    Depth getDepth(String symbol);

    Depth getDepth(String symbol, int depth);

}
