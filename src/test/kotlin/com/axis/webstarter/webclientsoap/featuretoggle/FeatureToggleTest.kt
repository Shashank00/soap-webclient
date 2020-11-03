package com.axis.webstarter.webclientsoap.featuretoggle

import io.mockk.every
import java.net.URI
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpHeaders
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.springframework.web.reactive.function.client.ClientRequest

class FeatureToggleTest {


    @Test
    fun `toggle should be on if if isFeatureToggleEnabled is true and no toggle-api headers present in request`() {
        val featureToggle = FeatureToggle("http://mock_url/test/service", true)

        val clientRequest = ClientRequest.create(HttpMethod.GET, URI("http://live-url/live/path"))
                .header("dummy-header", "dummyValue").build()

        assertTrue(featureToggle.isToggleOn(clientRequest))
    }

    @Test
    fun `toggle should be on if isFeatureToggleEnabled is true and toggle-api header is present and set to true`() {

        val featureToggle = FeatureToggle("http://mock_url/test/service", true)
        val clientRequest = mockk<ClientRequest>()
        val dummyHeaders = mockk<HttpHeaders>()
        every { dummyHeaders["toggle-api"] } returns listOf("true")
        every { clientRequest.headers() } returns dummyHeaders

        assertTrue(featureToggle.isToggleOn(clientRequest))
    }

    @Test
    fun `toggle should be off if isFeatureToggleEnabled is true but toggle-api header is present and set to false`() {

        val featureToggle = FeatureToggle("http://mock_url/test/service", true)
        val clientRequest = mockk<ClientRequest>()
        val dummyHeaders = mockk<HttpHeaders>()
        every { dummyHeaders["toggle-api"] } returns listOf("false")
        every { clientRequest.headers() } returns dummyHeaders

        assertFalse(featureToggle.isToggleOn(clientRequest))
    }

    @Test
    fun `toggle should be off if isFeatureToggleEnabled is false regardless of header present or not`() {

        val featureToggle = FeatureToggle("http://mock_url/test/service", false)
        val clientRequest = mockk<ClientRequest>()
        val dummyHeaders = mockk<HttpHeaders>()
        every { dummyHeaders["toggle-api"] } returns listOf("false")
        every { clientRequest.headers() } returns dummyHeaders

        assertFalse(featureToggle.isToggleOn(clientRequest))
    }
}