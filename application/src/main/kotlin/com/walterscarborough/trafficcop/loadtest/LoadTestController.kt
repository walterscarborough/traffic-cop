package com.walterscarborough.trafficcop.loadtest

import com.fasterxml.jackson.databind.ObjectMapper
import com.walterscarborough.trafficcop.configuration.LogProvider
import com.walterscarborough.trafficcop.reports.ReportsSettings
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class LoadTestController(
    private val loadTestService: LoadTestService,
    private val objectMapper: ObjectMapper,
    private val reportsSettings: ReportsSettings
) {
    @PostMapping("/run-load-test")
    private fun runLoadTest(@RequestBody runLoadRequest: RunLoadRequest): ResponseEntity<String> {
        LogProvider.getLogger(this.javaClass).info(
            "Running simulation in {} with request {}",
            reportsSettings.getReportsDir(),
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(runLoadRequest)
        )

        cacheRunLoadRequest(runLoadRequest)

        return loadTestService.executeGatlingScenario(runLoadRequest)
    }
}
