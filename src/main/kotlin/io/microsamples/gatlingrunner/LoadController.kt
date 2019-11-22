package io.microsamples.gatlingrunner

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.app.Gatling
import io.microsamples.gatlingrunner.load.GatlingContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.File
import java.util.stream.Collectors

@Controller
class LoadController(
        private val service: AsyncService,
        private val objectMapper: ObjectMapper = JacksonConfiguration().getObjectMapperWithJsr(),

        @Value("\${reports.dir:src/main/resources/static}")
        private val reportsDir: String? = null
) {
    @PostMapping("/run-test-post")
    private fun runTest(@RequestBody payload: String): ResponseEntity<String> {
        val simulationClazz = "io.microsamples.testz.simulation.RemoteChachkiesSimulation"
        GatlingContext.INSTANCE.payload = payload
        LogProvider.getLogger(this.javaClass).info("Running simulation: {} in {} with payload {}"
                , simulationClazz
                , reportsDir
                , objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload))

        return executeGatlingScenario(simulationClazz)
    }

    @GetMapping("/reports")
    fun reports(model: Model): String {
        val directories = File(reportsDir).listFiles { obj: File -> obj.isDirectory }
        val reports = listOf(*directories).stream().map { obj: File -> obj.name }.collect(Collectors.toList())
        model.addAttribute("reports", reports)

        return "index"
    }

    private fun executeGatlingScenario(simulation: String): ResponseEntity<String> {
        val args = arrayOf("-s", simulation, "-rf", reportsDir!!)
        runTestNoExit(args)
        return ResponseEntity.ok("Report Scheduled...  Check /reports in few minutes")
    }

    private fun runTestNoExit(args: Array<String>) {
        val secManager = MySecurityManager()
        System.setSecurityManager(secManager)
        service.doWork(Runnable {
            try {
                Gatling.main(args)
            } catch (se: SecurityException) { //prevent program from exiting
            }
        })
    }
}
