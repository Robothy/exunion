package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.core.exception.ServiceNotFoundException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeServiceProviderTest {
    @Test
    void newInstance() throws IOException {
        DepthService depthService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, DepthService.class);
        assertNotNull(depthService);
        assertThrows(ServiceNotFoundException.class, () -> ExchangeServiceProvider.newInstance(Unknown.SINGLETON, DepthService.class));

        Result<Depth> result = depthService.getDepth(Symbol.of("USDT", "BTC"));
        assertNotNull(result);
        assertNotNull(result.getStatus());
        assertNotNull(result.getCode());
        assertNotNull(result.getMessage());
    }

    @Test
    void testEnhancer() throws IOException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MockedDepthService.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            System.out.println(method);
            if(method.getReturnType() == Result.class){
                try {
                    proxy.invokeSuper(obj, args);
                }catch (RuntimeException e){
                    if(e.getClass() == InvalidOperationException.class){
                        return new Result<>("invalid-operation", e.getMessage());
                    }
                    throw e;
                }
            }
            return proxy.invokeSuper(obj, args);
        });

        MockedDepthService depthService = (MockedDepthService) enhancer.create();

        assertSame(depthService.getClass(), enhancer.create().getClass());

        Result<Depth> result = depthService.getDepth(Symbol.of(Currency.BTC, Currency.ETH));
        assertNotNull(result);
        assertNotNull(result.getStatus());
        assertNotNull(result.getCode());
        assertNotNull(result.getMessage());
    }

}