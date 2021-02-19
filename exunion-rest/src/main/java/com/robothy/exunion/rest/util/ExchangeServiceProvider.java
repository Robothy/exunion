package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.annotation.Version;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.exception.ServiceNotFoundException;
import com.robothy.exunion.rest.ExchangeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class ExchangeServiceProvider {

    /**
     * cache for quickly find the latest version of implementation.
     */
    private static final Map<SupportedExchange, Map<Class<?>, Class<? extends ExchangeService>>> CACHE = new HashMap<>();

    /**
     * Provide a trading service instance by exchange and the trading type (an interface).
     *
     * @param exchange the supported exchange.
     * @param clazz    the trading type.
     * @return a new trading instance.
     * @see SupportedExchange
     * @see com.robothy.exunion.rest.spot.SpotTradingService
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(SupportedExchange exchange, Class<T> clazz) {

        // load all trading services at once.
        if (!CACHE.containsKey(exchange) || !CACHE.get(exchange).containsKey(clazz)) {

            ServiceLoader<ExchangeService> serviceLoader = ServiceLoader.load(ExchangeService.class);
            Iterator<ExchangeService> it = serviceLoader.iterator();
            List<VersionedService> services = new ArrayList<>();
            while (it.hasNext()) {
                ExchangeService exchangeService = it.next();
                if (clazz.isAssignableFrom(exchangeService.getClass())) {
                    Version version = it.getClass().getAnnotation(Version.class);
                    services.add(new VersionedService(version == null ? "1.0" : version.value(), exchangeService));
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

        ExchangeService exchangeService = null;
        try {
            exchangeService = CACHE.get(exchange).get(clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // Ignore these exception, because the SPI invocation in prior steps checked instantiation and permission.
        }

        return (T) exchangeService;
    }

    private static class VersionedService {
        String version;
        ExchangeService service;

        VersionedService(String version, ExchangeService service) {
            this.version = version;
            this.service = service;
        }
    }

}
