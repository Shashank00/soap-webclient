package com.axis.webstarter.webclientsoap.utilities

import java.io.StringWriter
import freemarker.template.Configuration
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer

class TemplateGenerator(val freeMarkerConfigurer: FreeMarkerConfigurer) {

    fun getTemplate(templatePath: String, values: Any): String {
        val sw = StringWriter()
        val configuration: Configuration = freeMarkerConfigurer.configuration
        configuration.numberFormat = "0.###"
        val template = configuration.getTemplate(templatePath)
        template.process(values, sw)
        return sw.toString()
    }

}