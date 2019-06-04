package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;
import io.netty.channel.ChannelException;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.client.ExchangeFunction;

import static com.github.mkopylec.charon.forwarding.RequestForwardingException.requestForwardingError;

public class HttpRequestExecution {

    private String mappingName;
    private CustomConfiguration customConfiguration;
    private ExchangeFunction exchange;

    HttpRequestExecution(String mappingName, CustomConfiguration customConfiguration, ExchangeFunction exchange) {
        this.mappingName = mappingName;
        this.customConfiguration = customConfiguration;
        this.exchange = exchange;
    }

    public Mono<HttpResponse> execute(HttpRequest request) {
        return exchange.exchange(request)
                .map(response -> response instanceof HttpResponse
                        ? (HttpResponse) response
                        : new HttpResponse(response))
                .doOnError(ChannelException.class, e -> {
                    throw requestForwardingError("Error executing request: " + e.getMessage(), e);
                });
    }

    public String getMappingName() {
        return mappingName;
    }

    public <P> P getCustomProperty(String name) {
        return customConfiguration.getProperty(name);
    }
}
