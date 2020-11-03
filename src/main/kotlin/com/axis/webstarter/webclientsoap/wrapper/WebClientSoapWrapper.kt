package com.axis.webstarter.webclientsoap.wrapper

import java.time.Duration
import java.io.IOException
import reactor.core.publisher.Mono
import javax.xml.parsers.ParserConfigurationException
import com.axis.webstarter.webclientsoap.utilities.XmlProcessor
import org.springframework.web.reactive.function.client.WebClient
import com.axis.webstarter.webclientsoap.decoders.JaxbXmlToEntityMapper


class WebClientSoapWrapper(private val webClient: WebClient) {

    private val defaultRequestTimeout = Duration.ofMillis(180000L)

    @Throws(ParserConfigurationException::class, IOException::class)
    fun <T> getResponse(url: String,
                        requestXml: String,
                        tag: String,
                        responseType: Class<T>,
                        providedHeaders: Map<String, String> = emptyMap(),
                        requestTimeout: Duration? = null): Mono<T> {

        return webClient.post()
                .uri(url)
                .headers { h ->
                    providedHeaders.map {
                        h.set(it.key, it.value)
                    }
                }
                .body(Mono.just(requestXml),
                        String::class.java)
                .retrieve()
                .bodyToMono(String::class.java).log()
                .map { data ->
                    JaxbXmlToEntityMapper.unmarshall(XmlProcessor.extractXmlElement(data, tag),
                            responseType)
                }
                .timeout(requestTimeout ?: defaultRequestTimeout)
    }
}