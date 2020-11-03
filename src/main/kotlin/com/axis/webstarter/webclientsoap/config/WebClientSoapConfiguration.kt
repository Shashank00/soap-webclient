package com.axis.webstarter.webclientsoap.config

import io.netty.channel.ChannelOption
import reactor.netty.http.client.HttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient
import com.axis.webstarter.webclientsoap.filters.ToggleUrlFilter
import com.axis.webstarter.webclientsoap.wrapper.WebClientSoapWrapper
import org.springframework.http.client.reactive.ReactorClientHttpConnector


@Configuration
@ComponentScan("com.axis.webstarter.*")
class WebClientSoapConfiguration {
    val connector = ReactorClientHttpConnector(HttpClient.create()
            .tcpConfiguration { client ->
                client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            });

    @Bean
    @Primary
    fun webClient(toggleUrlFilter: ToggleUrlFilter) = WebClient
            .builder()
            .clientConnector(connector)
            .filter(toggleUrlFilter)
            .build();

    @Bean
    @Primary
    fun webClientWrapper(webClient: WebClient) = WebClientSoapWrapper(webClient);
}