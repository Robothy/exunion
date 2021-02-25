package com.robothy.exunion.huobi;

import com.robothy.exunion.core.auth.Token;

import java.util.Optional;

public class HuobiToken extends Token {

    public static final Token INSTANCE = new HuobiToken();

    private HuobiToken(){
        super(Optional.ofNullable(System.getProperty("huobi.api.key")).orElse("fake-key"), Optional.ofNullable(System.getProperty("huobi.api.secret")).orElse("fake-secret"));
    }

}
