package com.axis.webstarter.webclientsoap.config

import io.netty.channel.ChannelOption
import reactor.netty.http.client.HttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import com.axis.webstarter.webclientsoap.wrapper.WebClientSoapWrapper


@Configuration
@ComponentScan("com.axis.webstarter.*")
class WebClientSoapConfiguration {
    val connector = ReactorClientHttpConnector(HttpClient.create()
            .tcpConfiguration { client ->
                client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            });
    
    @Bean
    @Primary
    fun webClient(exchangeStrategies: ExchangeStrategies) = WebClient
            .builder()
            .clientConnector(connector)
            .exchangeStrategies(exchangeStrategies)
            .build();

    @Bean
    @Primary
    fun webClientWrapper(webClient: WebClient) = WebClientSoapWrapper(webClient);
}