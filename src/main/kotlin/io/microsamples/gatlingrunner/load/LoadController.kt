package io.microsamples.gatlingrunner.load

import io.gatling.app.Gatling
import io.microsamples.gatlingrunner.configuration.LogProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class LoadController(
        private val service: AsyncService
) {
    @Value("\${reports.dir:src/main/resources/static}")
    private lateinit var reportsDir: String

    @PostMapping("/run-load-test")
    private fun runTest(@RequestBody payload: String): ResponseEntity<String> {

        val simulationClazz = "io.microsamples.testz.simulation.RemoteChachkiesSimulation"
        GatlingContext.INSTANCE.payload = payload
        LogProvider.getLogger(this.javaClass).info(
                "Running simulation: {} in {} with payload {}"
                , simulationClazz
                , reportsDir
                , payload
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
