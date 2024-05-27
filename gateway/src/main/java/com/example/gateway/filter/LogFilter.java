package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Order(-1)
@Component
public class LogFilter extends AbstractGatewayFilterFactory<LogFilter.Config> {

    private final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    public LogFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            logger.info("[Request] {}, {} {}"
                    , exchange.getRequest().getId()
                    , exchange.getRequest().getMethod()
                    , exchange.getRequest().getPath());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("[Response] {}, {} {} {}"
                        , exchange.getRequest().getId()
                        , exchange.getRequest().getMethod()
                        , exchange.getRequest().getPath()
                        , exchange.getResponse().getStatusCode());
            }));
        });
    }

    public static class Config {
    }

}
