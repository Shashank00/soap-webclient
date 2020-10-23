package com.axis.webstarter.webclientsoap.config

import io.netty.channel.ChannelOption
import reactor.netty.http.client.HttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import com.axis.webstarter.webclientsoap.wrapper.WebClientSoapWrapper
import com.axis.webstarter.webclientsoap.encoders.Jaxb2SoapEncoder
import org.springframework.http.codec.xml.Jaxb2XmlDecoder


@Configuration
@ComponentScan("com.axis.webstarter.*")
class WebClientSoapConfiguration {
    val connector = ReactorClientHttpConnector(HttpClient.create()
            .tcpConfiguration { client ->
                client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            });

    var exchangeStrategies = ExchangeStrategies.builder().codecs { clientCodecConfigure: ClientCodecConfigurer -> clientCodecConfigure.customCodecs().register(Jaxb2SoapEncoder()); clientCodecConfigure.customCodecs().register(Jaxb2XmlDecoder()) }.build();
    
    @Bean
    @Primary
    fun webClient() = WebClient
            .builder()
            .clientConnector(connector)
            .exchangeStrategies(exchangeStrategies)
            .build();

    @Bean
    @Primary
    fun webClientWrapper(webClient: WebClient) = WebClientSoapWrapper(webClient);
}