package com.robothy.exunion.rest.spi;

import com.robothy.exunion.core.annotation.Version;
import com.robothy.exunion.core.exception.ServiceNotFoundException;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.rest.ExchangeService;

import java.util.*;

/**
 * A SPI that create exchange service instance.
 */
@SuppressWarnings("unchecked")
public class ExchangeServiceProvider {

    /**
     * cache for quickly find the latest version of implementation.
     */
    private static final Map<Exchange, Map<Class<?>, Class<? extends ExchangeService>>> CACHE = new HashMap<>();

    /**
     * Create an exchange service instance and call the init() method before return the instance.
     * <p>
     * If a type of service in specific exchange has multiple implementations, the one with latest
     * version will be chosen. The version number defined by the {@link Version} annotation.
     *
     * @param exchange the exchange that provide the service.
     * @param serviceClazz the exchange service clazz. For example: <code>DepthService.class</code>
     * @param options the options to initialize the exchange service instance.
     * @param <T> the exchange service type. For example: {@link com.robothy.exunion.rest.market.DepthService}
     * @return an initialized exchange service instance.
     */
    public static <T> T newInstance(Exchange exchange, Class<T> serviceClazz, Options options){
        // load all exchange services at once
        if(!CACHE.containsKey(exchange) || !CACHE.get(exchange).containsKey(serviceClazz)){
            ServiceLoader<ExchangeService> serviceLoader = ServiceLoader.load(ExchangeService.class);
            Iterator<ExchangeService> it = serviceLoader.iterator();

            List<VersionedService> services = new ArrayList<>();
            while (it.hasNext()){
                ExchangeService service = it.next();
                if(exchange.id().equals(service.exchange().id()) && serviceClazz.isAssignableFrom(service.getClass())){
                    Version version = service.getClass().getAnnotation(Version.class);
                    services.add(new VersionedService(version == null ? "1.0" : version.value(), service));
                }
            }

            if (services.size() == 0) {
                throw new ServiceNotFoundException("No trading service implementation found for " + serviceClazz + " of " + exchange.id() + " exchange.");
            }

            // sort by version in DESC
            services.sort((a, b) -> b.version.compareTo(a.version));

            CACHE.putIfAbsent(exchange, new HashMap<>());
            CACHE.get(exchange).put(serviceClazz, services.get(0).service.getClass());
        }

        ExchangeService exchangeService = null;
        try {
            exchangeService = CACHE.get(exchange).get(serviceClazz).newInstance();
            exchangeService.init(options);
        } catch (InstantiationException | IllegalAccessException e) {
            // Ignore these exception, because the SPI invocation in prior steps checked instantiation and permission.
        }

        return (T) exchangeService;
    }

    /**
     * This method will invoke {@link ExchangeServiceProvider#newInstance(Exchange, Class, Options)} and pass <code>null</code> options.
     */
    public static <T> T newInstance(Exchange exchange, Class<T> clazz){
        return newInstance(exchange, clazz, null);
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