package io.microsamples.gatlingrunner.reports

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.io.File

@Controller
class ReportsController {

    @Value("\${reports.dir:src/main/resources/static}")
    private lateinit var reportsDir: String

    @GetMapping("/reports")
    fun reports(model: Model): String {
        val directories = File(reportsDir)
            .listFiles { obj: File -> obj.isDirectory }!!
            .asList()

        val reports = directories.map { obj: File -> obj.name }

        model.addAttribute("reports", reports)

        return "index"
    }
}
