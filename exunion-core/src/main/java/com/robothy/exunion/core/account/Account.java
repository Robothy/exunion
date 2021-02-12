package com.robothy.exunion.core.account;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Account {

    private Map<String, Asset> assets;

    public Map<String, Asset> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, Asset> assets) {
        this.assets = assets;
    }

    public Asset getAsset(String symbol){
        Objects.requireNonNull(assets, "The assets shouldn't be null.");
        return this.assets.get(symbol);
    }

    public void addAsset(String symbol, Asset asset){
        if(this.assets == null){
            this.assets = new HashMap<>();
        }
        this.assets.put(symbol, asset);
    }

    public void addAsset(Asset asset){
        this.addAsset(asset.getSymbol(), asset);
    }

}
