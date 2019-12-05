package io.microsamples.gatlingrunner.load

import com.fasterxml.jackson.databind.ObjectMapper
import io.microsamples.gatlingrunner.configuration.LogProvider
import io.microsamples.testz.GatlingWrapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class LoadController(
        private val service: AsyncService,
        private val objectMapper: ObjectMapper
) {
    @Value("\${reports.dir:src/main/resources/static}")
    private lateinit var reportsDir: String

    @Suppress("unused")
    @PostMapping("/run-load-test")
    private fun runLoadTest(@RequestBody runLoadRequest: RunLoadRequest): ResponseEntity<String> {
        cacheRunLoadRequest(runLoadRequest)

        LogProvider.getLogger(this.javaClass).info(
            "Running simulation in {} with request {}"
            , reportsDir
            , objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(runLoadRequest)
        )

        return executeGatlingScenario()
    }

    private fun executeGatlingScenario(): ResponseEntity<String> {
        val simulationClass = "io.microsamples.testz.simulation.GeneralSimulation"
        val args = arrayOf("-s", simulationClass, "-rf", reportsDir)
        runTestNoExit(args)
        return ResponseEntity.ok("Report Scheduled...  Check /reports in few minutes")
    }

    private fun runTestNoExit(args: Array<String>) {
        val secManager = MySecurityManager()
        System.setSecurityManager(secManager)
        service.doWork(
                Runnable {
                    try {
                        GatlingWrapper.startGatling(args)
                    } catch (securityException: SecurityException) {
                    }
                }
        )
    }
}
