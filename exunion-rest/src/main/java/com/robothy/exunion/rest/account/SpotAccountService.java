package com.robothy.exunion.rest.account;

import com.robothy.exunion.core.account.SpotAccount;
import com.robothy.exunion.rest.Result;

import java.io.IOException;
import java.util.Map;

public interface SpotAccountService {

    /**
     * Query the spot account of current API secret owner with extra information.
     *
     * @param extraInfo extra information to get the account.
     * @return spot account information.
     */
    Result<SpotAccount> query(Map<String, Object> extraInfo) throws IOException;

    /**
     * Query the spot account of current API secret owner without extra information.
     *
     * @return the spot account information.
     */
    Result<SpotAccount> query() throws IOException;

}
