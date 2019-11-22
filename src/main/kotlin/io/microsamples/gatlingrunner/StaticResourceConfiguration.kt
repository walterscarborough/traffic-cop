package io.microsamples.gatlingrunner

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class StaticResourceConfiguration : WebMvcConfigurer {
    @Value("\${reports.location:classpath:/static/}")
    private val reportsLocation: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        LogProvider.getLogger(this.javaClass).info("Loading reports from: {}", reportsLocation)
        registry.addResourceHandler("/**").addResourceLocations(reportsLocation)
    }
}
