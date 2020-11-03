package com.axis.webstarter.webclientsoap.controllers

import java.time.Duration
import java.io.IOException
import reactor.core.publisher.Mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.xml.parsers.ParserConfigurationException
import com.axis.webstarter.webclientsoap.wrapper.WebClientSoapWrapper


@RestController
class Controller {

    @Autowired
    lateinit var reactiveSoapClient: WebClientSoapWrapper

    @PostMapping(value = ["/test"])
    @Throws(ParserConfigurationException::class, IOException::class)
    fun callSoap(): Mono<T> {
        return reactiveSoapClient.getResponse<Any>("http://soapserver/real", "getCountryRequest.xml", "getCountryResponse", String.javaClass, mutableMapOf<String,String>(), Duration.ofMillis(1000))
    }
}