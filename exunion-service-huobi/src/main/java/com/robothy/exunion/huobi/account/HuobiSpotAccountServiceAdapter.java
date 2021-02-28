package com.robothy.exunion.huobi.account;

import com.huobi.client.AccountClient;
import com.huobi.client.req.account.AccountBalanceRequest;
import com.huobi.model.account.Account;
import com.huobi.model.account.AccountBalance;
import com.huobi.model.account.Balance;
import com.robothy.exunion.core.account.Asset;
import com.robothy.exunion.core.account.SpotAccount;
import com.robothy.exunion.huobi.AbstractHuobiAuthorizedExchangeService;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.account.SpotAccountService;
import com.robothy.exunion.rest.spi.Options;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HuobiSpotAccountServiceAdapter extends AbstractHuobiAuthorizedExchangeService
        implements SpotAccountService {

    private AccountClient accountService;

    @Override
    public void init(Options options) {
        super.init(options);
        this.accountService = AccountClient.create(huobiOptions);
    }

    @Override
    public Result<SpotAccount> query(Map<String, Object> extraInfo) {
        return query();
    }

    @Override
    public Result<SpotAccount> query() {
        Account huobiSpotAccount = accountService.getAccounts()
                .stream().filter(account -> HuobiAccountType.SPOT.equals(account.getType())).findAny()
                .orElse(null);
        if (null == huobiSpotAccount) {
            return new Result<>("no-spot-account", "no spot account");
        }

        AccountBalance accountBalance = accountService.getAccountBalance(AccountBalanceRequest.builder().accountId(huobiSpotAccount.getId()).build());
        SpotAccount spotAccount = new SpotAccount();
        spotAccount.setAccountId(String.valueOf(huobiSpotAccount.getId()));
        spotAccount.setAssets(new HashMap<>());
        Map<String, Asset> assets = spotAccount.getAssets();

        Map<String, List<Balance>> grouped = accountBalance.getList().stream().collect(Collectors.groupingBy(Balance::getCurrency));
        grouped.forEach((currency, balanceList) -> {
            currency = currency.toUpperCase();
            assets.putIfAbsent(currency, new Asset(currency));
            final Asset asset = assets.get(currency);
            balanceList.forEach(balance -> {
                if (HuobiBalanceType.TRADE.equals(balance.getType())) {
                    asset.setFreeAmount(balance.getBalance());
                } else if (HuobiBalanceType.FROZEN.equals(balance.getType()) || HuobiBalanceType.LOCK.equals(balance.getType())) {
                    asset.setLockedAmount(Optional.ofNullable(asset.getLockedAmount()).orElse(BigDecimal.ZERO).add(balance.getBalance()));
                }
            });
        });
        return new Result<>(spotAccount, accountBalance);
    }
}
