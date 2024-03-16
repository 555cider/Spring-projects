package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class LoggingGatewayFilter extends AbstractGatewayFilterFactory<LoggingGatewayFilter.Config> {

    final Logger logger = LoggerFactory.getLogger(LoggingGatewayFilter.class);

    public LoggingGatewayFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (config.isPreLogger()) {
                logger.info("[Request] >>> {} {}, IP: {}"
                        , exchange.getRequest().getMethod()
                        , exchange.getRequest().getURI()
                        , Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    logger.info("[Response] >>> {} {}, {}"
                            , exchange.getRequest().getMethod()
                            , exchange.getRequest().getURI()
                            , exchange.getResponse().getStatusCode());
                }
            }));
        };
    }

    public static class Config {
        private boolean preLogger = false;
        private boolean postLogger = false;

        public Config() {
        }

        public Config(boolean preLogger, boolean postLogger) {
            this.preLogger = preLogger;
            this.postLogger = postLogger;
        }

        public boolean isPreLogger() {
            return preLogger;
        }

        public void setPreLogger(boolean preLogger) {
            this.preLogger = preLogger;
        }

        public boolean isPostLogger() {
            return postLogger;
        }

        public void setPostLogger(boolean postLogger) {
            this.postLogger = postLogger;
        }
    }

}
