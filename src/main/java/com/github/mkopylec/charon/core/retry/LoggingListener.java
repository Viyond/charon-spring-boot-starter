package com.github.mkopylec.charon.core.retry;

import org.slf4j.Logger;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

import static com.github.mkopylec.charon.configuration.CharonProperties.Retrying.MAPPING_NAME_RETRY_ATTRIBUTE;
import static org.slf4j.LoggerFactory.getLogger;

public class LoggingListener extends RetryListenerSupport {

    private static final Logger log = getLogger(LoggingListener.class);

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.debug("Forwarding: Attempt {} to proxy '{}' has failed. {}",
                context.getRetryCount() + 1, context.getAttribute(MAPPING_NAME_RETRY_ATTRIBUTE), throwable.getMessage());
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        if (throwable != null) {
            log.error("Forwarding: All {} attempts to proxy '{}' has failed.", context.getRetryCount(), context.getAttribute(MAPPING_NAME_RETRY_ATTRIBUTE));
        } else {
            log.debug("Forwarding: Attempt {} to proxy '{}' has succeeded", context.getRetryCount() + 1, context.getAttribute(MAPPING_NAME_RETRY_ATTRIBUTE));
        }
    }
}