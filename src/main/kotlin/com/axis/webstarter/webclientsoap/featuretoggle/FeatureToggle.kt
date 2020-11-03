package com.axis.webstarter.webclientsoap.featuretoggle

import org.springframework.web.reactive.function.client.ClientRequest

class FeatureToggle(val mockBaseUrl: String, val isFeatureToggleEnabled: Boolean) {

    companion object {
        private const val API_TOGGLE_HEADER = "toggle-api"
    }

    // Checking feature toggle header in request
    fun isToggleOn(request: ClientRequest): Boolean {
        val toggle = request.headers()[API_TOGGLE_HEADER]?.first()
        try {
            if (toggle == null || toggle.toBoolean()) {
                return true
            }
        } catch (ex: Exception) {
            return false;
        }
        return false;
    }
}