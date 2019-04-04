package com.github.mkopylec.charon.interceptors.rewrite;

import java.util.List;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE2;

class RemovingResponseCookieRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RemovingResponseCookieRewriter.class);

    RemovingResponseCookieRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Removing response cookies for '{}' forwarding", execution.getForwardingName());
        HttpResponse response = execution.execute(request);
        removeCookies(response, SET_COOKIE);
        removeCookies(response, SET_COOKIE2);
        log.trace("[End] Removing response cookies for '{}' forwarding", execution.getForwardingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RESPONSE_COOKIE_REWRITER_ORDER;
    }

    private void removeCookies(HttpResponse response, String header) {
        List<String> removedCookies = response.getHeaders().remove(header);
        if (isNotEmpty(removedCookies)) {
            log.debug("Cookies {} removed from response", removedCookies);
        }
    }
}
