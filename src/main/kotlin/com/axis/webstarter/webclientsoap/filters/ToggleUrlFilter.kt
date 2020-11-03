package com.axis.webstarter.webclientsoap.filters

import java.net.URI
import reactor.core.publisher.Mono
import com.axis.webstarter.webclientsoap.featuretoggle.FeatureToggle
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.ExchangeFilterFunction

class ToggleUrlFilter(private val featureToggle: FeatureToggle) : ExchangeFilterFunction {

    override fun filter(request: ClientRequest, exchangeFunction: ExchangeFunction): Mono<ClientResponse> {
        return Mono.subscriberContext()
                .flatMap {
                    var toggle = featureToggle.isFeatureToggleEnabled;

                    if (toggle) {
                        toggle = featureToggle.isToggleOn(request)
                    }
                    val newRequest = if (!toggle) {
                        request
                    } else {
                        requestFromMockedUrl(request)
                    }
                    exchangeFunction.exchange(newRequest)
                }
    }

    private fun requestFromMockedUrl(request: ClientRequest): ClientRequest {

        return ClientRequest.from(request)
                .url(URI(featureToggle.mockBaseUrl))
                .build()
    }
}