package com.axis.webstarter.webclientsoap.wrapper

import java.time.Duration
import java.io.IOException
import reactor.core.publisher.Mono
import org.springframework.http.HttpStatus
import javax.xml.parsers.ParserConfigurationException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient


class WebClientSoapWrapper(private val webClient: WebClient) {

    private val defaultRequestTimeout = Duration.ofMillis(180000L)

    @Throws(ParserConfigurationException::class, IOException::class)
    fun <T> call(url: String,
                 request: Any,
                 requestType: Class<T>,
                 responseType: Class<T>,
                 providedHeaders: Map<String, String> = emptyMap(),
                 requestTimeout: Duration? = null): Mono<T> {

        return webClient.post()
                .uri(url)
                .headers { h ->
                    providedHeaders.map {
                    h.set(it.key, it.value) } }
                .body(Mono.just(request), requestType)
                .retrieve()
                .onStatus({ obj: HttpStatus -> obj.isError },
                        { clientResponse: ClientResponse ->
                            clientResponse
                                    .bodyToMono(String::class.java)
                                    .flatMap { errorResponseBody: String? ->
                                        Mono.error<Throwable?>(
                                                ResponseStatusException(
                                                        clientResponse.statusCode(),
                                                        errorResponseBody))
                                    }
                        })
                .bodyToMono(responseType)
                .timeout(requestTimeout ?: defaultRequestTimeout)
                .doOnError(ResponseStatusException::class.java) { error -> println("error : $error") }
    }
}