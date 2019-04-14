package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.CustomConfigurer;
import com.github.mkopylec.charon.forwarding.RestTemplateConfigurer;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

public class CharonConfigurer extends Configurer<CharonConfiguration> {

    private CharonConfigurer() {
        super(new CharonConfiguration());
    }

    public static CharonConfigurer charonConfiguration() {
        return new CharonConfigurer();
    }

    public CharonConfigurer filterOrder(int filterOrder) {
        configuredObject.setFilterOrder(filterOrder);
        return this;
    }

    public CharonConfigurer set(RestTemplateConfigurer restTemplateConfigurer) {
        configuredObject.setRestTemplateConfiguration(restTemplateConfigurer.configure());
        return this;
    }

    public CharonConfigurer set(RequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        configuredObject.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.configure());
        return this;
    }

    public CharonConfigurer add(RequestMappingConfigurer requestMappingConfigurer) {
        configuredObject.addRequestForwardingConfiguration(requestMappingConfigurer.configure());
        return this;
    }

    public CharonConfigurer set(CustomConfigurer customConfigurer) {
        configuredObject.setCustomConfiguration(customConfigurer.configure());
        return this;
    }
}