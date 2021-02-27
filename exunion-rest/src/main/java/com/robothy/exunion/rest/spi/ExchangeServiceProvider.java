package com.robothy.exunion.rest.spi;

import com.robothy.exunion.core.annotation.Version;
import com.robothy.exunion.core.exception.ServiceNotFoundException;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.rest.ExchangeService;
import com.robothy.exunion.rest.Result;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.*;

/**
 * A SPI that create exchange service instance.
 */
@SuppressWarnings("ALL")
public class ExchangeServiceProvider {

    /**
     * cache for quickly find the latest version of implementation.
     */
    private static final Map<Exchange, Map<Class<?>, Enhancer>> CACHE = new HashMap<>();

    private static final Set<ExchangeServiceExceptionHandler> EXCEPTION_HANDLERS = new HashSet<>();

    static {
        // Load exception handlers.
        ServiceLoader<ExchangeServiceExceptionHandler> load = ServiceLoader.load(ExchangeServiceExceptionHandler.class);
        for (ExchangeServiceExceptionHandler handler : load) {
            EXCEPTION_HANDLERS.add(handler);
        }
    }

    /**
     * Create an exchange service instance and call the init() method before return the instance.
     * <p>
     * If a type of service in specific exchange has multiple implementations, the one with latest
     * version will be chosen. The version number defined by the {@link Version} annotation.
     *
     * @param exchange     the exchange that provide the service.
     * @param serviceClazz the exchange service clazz. For example: <code>DepthService.class</code>
     * @param options      the options to initialize the exchange service instance.
     * @param <T>          the exchange service type. For example: {@link com.robothy.exunion.rest.market.DepthService}
     * @return an initialized exchange service instance.
     */
    public static <T> T newInstance(Exchange exchange, Class<T> serviceClazz, Options options) {
        // load all exchange services at once
        if (!CACHE.containsKey(exchange) || !CACHE.get(exchange).containsKey(serviceClazz)) {
            ServiceLoader<ExchangeService> serviceLoader = ServiceLoader.load(ExchangeService.class);
            Iterator<ExchangeService> it = serviceLoader.iterator();

            List<VersionedService> services = new ArrayList<>();
            while (it.hasNext()) {
                ExchangeService service = it.next();
                if (exchange.id().equals(service.exchange().id()) && serviceClazz.isAssignableFrom(service.getClass())) {
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
            CACHE.get(exchange).put(serviceClazz, enhance(services.get(0).service.getClass()));
        }

        ExchangeService exchangeService = (ExchangeService) CACHE.get(exchange).get(serviceClazz).create();
        exchangeService.init(options);
        return (T) exchangeService;
    }

    /**
     * This method will invoke {@link ExchangeServiceProvider#newInstance(Exchange, Class, Options)} and pass a <code>null</code> options.
     */
    public static <T> T newInstance(Exchange exchange, Class<T> clazz) {
        return newInstance(exchange, clazz, null);
    }

    /**
     * create a dynamic proxy class for the input clazz.
     *
     * @param clazz the clazz to be proxied.
     * @param <T>   the data type.
     * @return the enhancer for the clazz.
     */
    private static <T> Enhancer enhance(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            System.out.println(method);
            if (method.getReturnType() == Result.class) {
                try {
                    return proxy.invokeSuper(obj, args);
                } catch (RuntimeException e) {
                    Result<?> result = null;
                    boolean hasHandler = false;
                    for (ExchangeServiceExceptionHandler handler : EXCEPTION_HANDLERS) {
                        for (Class ex : handler.exceptions()) {
                            if (e.getClass() == ex) {
                                Result<?> tmp = handler.handle(e);
                                if (result == null) {
                                    result = tmp;
                                }
                                hasHandler = true;
                            }
                        }
                    }

                    if(hasHandler) return result;

                    // no handler for e.
                    throw e;
                }
            }
            return proxy.invokeSuper(obj, args);
        });
        return enhancer;
    }

    private static class VersionedService {
        private String version;
        private ExchangeService service;

        VersionedService(String version, ExchangeService service) {
            this.version = version;
            this.service = service;
        }
    }

}