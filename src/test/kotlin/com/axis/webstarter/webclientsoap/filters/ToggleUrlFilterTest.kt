package com.axis.webstarter.webclientsoap.filters

import java.net.URI
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import reactor.test.StepVerifier
import reactor.core.publisher.Mono
import org.junit.jupiter.api.Test
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpMethod
import com.axis.webstarter.webclientsoap.featuretoggle.FeatureToggle
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.server.ServerWebExchange

class ToggleUrlFilterTest {

    @Test
    fun `should not toggle url if isFeatureToggleEnabled is false in any case`() {

        val initialRequest = ClientRequest.create(HttpMethod.GET, URI("http://live-url/live/path"))
                .header("toggle-api", "true").build()

        val mockExchangeFunction = mockk<ExchangeFunction>()
        val mockExchange = mockk<ServerWebExchange>()
        val mockRequest = mockk<ServerHttpRequest>()
        val list = slot<ClientRequest>()

        every { mockExchange.request } returns mockRequest
        every {
            mockExchangeFunction.exchange(
                    capture(list)
            )
        } returns Mono.empty()


        val featureToggle = FeatureToggle("http://mock_url/test/service", false)

        val producer = ToggleUrlFilter(featureToggle)
                .filter(initialRequest, mockExchangeFunction)

        StepVerifier.create(producer)
                .then {
                    list.isCaptured shouldBe true
                    val expectedClientRequest = list.captured
                    expectedClientRequest.url().toString() shouldBe "http://live-url/live/path"

                }
                .verifyComplete()
    }

    @Test
    fun `should toggle url if isFeatureToggleEnabled is true & toggle-api header is not true or set in request`() {

        val initialRequest = ClientRequest.create(HttpMethod.GET, URI("http://live-url/live/path"))
                .header("dummy-header", "dummyValue").build()

        val mockExchangeFunction = mockk<ExchangeFunction>()
        val mockExchange = mockk<ServerWebExchange>()
        val mockRequest = mockk<ServerHttpRequest>()
        val list = slot<ClientRequest>()

        every { mockExchange.request } returns mockRequest
        every {
            mockExchangeFunction.exchange(
                    capture(list)
            )
        } returns Mono.empty()


        val featureToggle = FeatureToggle("http://mock_url/test/service", true)

        val producer = ToggleUrlFilter(featureToggle)
                .filter(initialRequest, mockExchangeFunction)

        StepVerifier.create(producer)
                .then {
                    list.isCaptured shouldBe true
                    val expectedClientRequest = list.captured
                    expectedClientRequest.url().toString() shouldBe "http://mock_url/test/service"

                }
                .verifyComplete()
    }

    @Test
    fun `should not toggle url if isFeatureToggleEnabled is true but toggle-api header is false in request`() {

        val initialRequest = ClientRequest.create(HttpMethod.GET, URI("http://live-url/live/path"))
                .header("toggle-api", "false").build()

        val mockExchangeFunction = mockk<ExchangeFunction>()
        val mockExchange = mockk<ServerWebExchange>()
        val mockRequest = mockk<ServerHttpRequest>()
        val list = slot<ClientRequest>()

        every { mockExchange.request } returns mockRequest
        every {
            mockExchangeFunction.exchange(
                    capture(list)
            )
        } returns Mono.empty()

        val featureToggle = FeatureToggle("http://mock_url/test/service", true)

        val producer = ToggleUrlFilter(featureToggle)
                .filter(initialRequest, mockExchangeFunction)

        StepVerifier.create(producer)
                .then {
                    list.isCaptured shouldBe true
                    val expectedClientRequest = list.captured
                    expectedClientRequest.url().toString() shouldBe "http://live-url/live/path"

                }
                .verifyComplete()
    }
}