package com.github.mkopylec.charon.test.stubs

import org.mockserver.integration.ClientAndServer
import org.mockserver.matchers.Times
import org.mockserver.model.Header
import org.mockserver.verify.VerificationTimes
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static org.mockserver.model.Delay.seconds
import static org.mockserver.model.Header.header
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class OutgoingServer {

    private ClientAndServer server

    OutgoingServer(int port) {
        server = startClientAndServer(port)
    }

    void stubResponse(HttpStatus status, Map<String, String> headers, String body, ResponseDelay delay, int times) {
        server.when(request('.*'), Times.exactly(times))
                .respond(response(body)
                        .withStatusCode(status.value())
                        .withHeaders(toHeaders(headers))
                        .withDelay(seconds(delay.seconds)))
    }

    void verifyRequest(HttpMethod method, String path, Map<String, String> headers, String body, int times) {
        server.verify(request(path)
                .withMethod(method ? method.name() : null)
                .withHeaders(toHeaders(headers))
                .withBody(body), VerificationTimes.exactly(times))
    }

    void verifyNoRequest() {
        server.verifyZeroInteractions()
    }

    void clear() {
        server.reset()
    }

    @Override
    String toString() {
        return "localhost:${server.localPort}"
    }

    private static List<Header> toHeaders(Map<String, String> headers) {
        return headers.collect { header(it.key, it.value.split(', ')) }
    }
}
