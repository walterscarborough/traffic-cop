package io.microsamples.gatlingrunner.load

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.app.Gatling
import io.microsamples.gatlingrunner.configuration.LogProvider
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

    @PostMapping("/run-load-test")
    private fun runTest(@RequestBody runLoadPostRequest: RunLoadPostRequest): ResponseEntity<String> {

        val simulationClazz = "io.microsamples.testz.simulation.PostRequestSimulation"
        GatlingContext.INSTANCE.payload = runLoadPostRequest.payload
        GatlingContext.INSTANCE.baseUrl = runLoadPostRequest.baseUrl
        GatlingContext.INSTANCE.endpoint = runLoadPostRequest.endpoint
        GatlingContext.INSTANCE.constantUsersPerSecond = runLoadPostRequest.constantUsersPerSecond
        GatlingContext.INSTANCE.constantUsersPerSecondDuration = runLoadPostRequest.constantUsersPerSecondDuration

        LogProvider.getLogger(this.javaClass).info(
            "Running simulation: {} in {} with request {}"
            , simulationClazz
            , reportsDir
            , objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(runLoadPostRequest)
        )

        return executeGatlingScenario(simulationClazz)
    }

    private fun executeGatlingScenario(simulation: String): ResponseEntity<String> {
        val args = arrayOf("-s", simulation, "-rf", reportsDir)
        runTestNoExit(args)
        return ResponseEntity.ok("Report Scheduled...  Check /reports in few minutes")
    }

    private fun runTestNoExit(args: Array<String>) {
        val secManager = MySecurityManager()
        System.setSecurityManager(secManager)
        service.doWork(
                Runnable {
                    try {
                        Gatling.main(args)
                    } catch (se: SecurityException) {
                    }
                }
        )
    }
}
