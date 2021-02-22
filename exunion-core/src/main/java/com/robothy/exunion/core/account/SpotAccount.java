package com.robothy.exunion.core.account;

import java.util.Map;

/**
 * Spot account model.
 */
public class SpotAccount extends Account {

    private Map<String, Asset> assets;

    public Map<String, Asset> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, Asset> assets) {
        this.assets = assets;
    }

    public Asset getAsset(String currency){
        return assets.get(currency);
    }

}
