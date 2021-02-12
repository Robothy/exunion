package com.robothy.exunion.rest.account;

import com.robothy.exunion.core.account.Account;

interface AccountService {

    Account getSpotAccount();

    Account getMarginAccount();

}
