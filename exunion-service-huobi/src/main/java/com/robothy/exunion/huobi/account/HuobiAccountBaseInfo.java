package com.robothy.exunion.huobi.account;

import com.google.api.client.util.Key;

public class HuobiAccountBaseInfo {

    @Key(Keys.ID)
    private Long id;

    @Key(Keys.STATE)
    private String state;

    @Key(Keys.TYPE)
    private String accountType;

    @Key(Keys.SUBTYPE)
    private String subtype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    interface Keys {

        String ID = "id";

        String STATE = "state";

        String TYPE = "type";

        String SUBTYPE = "subtype";

    }

}
