package com.robothy.exunion.api.market;

import exunion.metaobjects.Depth;

interface DepthService {

    Depth getDepth(String symbol);

    Depth getDepth(String symbol, int depth);

}