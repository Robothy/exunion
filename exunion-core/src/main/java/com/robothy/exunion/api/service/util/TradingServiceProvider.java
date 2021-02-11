package com.robothy.exunion.api.service.util;

import com.robothy.exunion.api.annotation.Version;
import com.robothy.exunion.api.meta.SupportedExchange;
import com.robothy.exunion.api.service.exception.ServiceNotFoundException;
import com.robothy.exunion.api.trade.spot.SpotTradingService;
import com.robothy.exunion.api.trade.TradingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class TradingServiceProvider {

    /**
     * cache for quickly find the latest version of implementation.
     */
    private static final Map<SupportedExchange, Map<Class<?>, Class<? extends TradingService>>> CACHE = new HashMap<>();

    /**
     * Provide a trading service instance by exchange and the trading type (an interface).
     *
     * @param exchange the supported exchange.
     * @param clazz    the trading type.
     * @return a new trading instance.
     * @see SupportedExchange
     * @see SpotTradingService
     */
    public static TradingService newInstance(SupportedExchange exchange, Class<?> clazz) {

        // load all trading services at once.
        if (!CACHE.containsKey(exchange) || !CACHE.get(exchange).containsKey(clazz)) {

            ServiceLoader<TradingService> serviceLoader = ServiceLoader.load(TradingService.class);
            Iterator<TradingService> it = serviceLoader.iterator();
            List<VersionedService> services = new ArrayList<>();
            while (it.hasNext()) {
                TradingService tradingService = it.next();

                for (Class<?> i : tradingService.getClass().getInterfaces()) {
                    if (i == clazz) {
                        Version version = i.getAnnotation(Version.class);
                        services.add(new VersionedService(version == null ? "1.0" : version.value(), tradingService));
                        break;
                    }
                }
            }

            if (services.size() == 0) {
                throw new ServiceNotFoundException("No trading service implementation found for " + clazz + " of " + exchange + " exchange.");
            }

            // sort by version in DESC
            services.sort((a, b) -> b.version.compareTo(a.version));

            // cache the target
            CACHE.putIfAbsent(exchange, new HashMap<>());
            CACHE.get(exchange).put(clazz, services.get(0).service.getClass());
        }

        TradingService tradingService = null;
        try {
            tradingService = CACHE.get(exchange).get(clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // Ignore these exception, because the SPI invocation in prior steps checked instantiation and permission.
        }
        return tradingService;
    }

    private static class VersionedService {
        String version;
        TradingService service;

        VersionedService(String version, TradingService service) {
            this.version = version;
            this.service = service;
        }
    }

}
