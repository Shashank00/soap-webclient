package com.axis.webstarter.webclientsoap.config

import org.springframework.context.annotation.Bean
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ComponentScan
import com.axis.webstarter.webclientsoap.filters.ToggleUrlFilter
import com.axis.webstarter.webclientsoap.featuretoggle.FeatureToggle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.context.config.annotation.RefreshScope

@Configuration
@RefreshScope
@ComponentScan("com.axis.webstarter.*")
@ConditionalOnProperty("feature.toggle.enabled")
class FeatureToggleConfiguration {

    @Bean
    fun getFeatureToggle(@Value("\${axis.feature.toggle.baseUrl}") mockBaseUrl: String, @Value("\${feature.toggle.enabled}") isToggleEnabled: Boolean): FeatureToggle {
        return FeatureToggle(mockBaseUrl, isToggleEnabled);
    }

    @Bean
    fun toggleUrlFilter(featureToggle: FeatureToggle): ToggleUrlFilter {
        return ToggleUrlFilter(featureToggle)
    }
}
