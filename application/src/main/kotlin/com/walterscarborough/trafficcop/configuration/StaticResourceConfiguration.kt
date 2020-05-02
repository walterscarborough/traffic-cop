package com.walterscarborough.trafficcop.configuration

import com.walterscarborough.trafficcop.reports.REPORTS_DIR_PATH
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class StaticResourceConfiguration : WebMvcConfigurer {

    @Value(REPORTS_DIR_PATH)
    private lateinit var reportsDir: String

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val reportsLocation = "file:${reportsDir}"

        LogProvider.getLogger(this.javaClass).info("Loading reports from: {}", reportsLocation)
        registry.addResourceHandler("/**").addResourceLocations(reportsLocation)
    }
}
