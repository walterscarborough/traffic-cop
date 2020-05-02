package com.walterscarborough.trafficcop.reports

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.io.File

@Controller
class ReportsController(
    private val reportsSettings: ReportsSettings
) {
    @GetMapping(value = ["/", "/reports"])
    fun reports(model: Model): String {
        val directories = File(reportsSettings.getReportsDir())
            .listFiles { obj: File -> obj.isDirectory }
            .orEmpty()
            .asList()

        val reports = directories.map { obj: File -> obj.name }

        model.addAttribute("reports", reports)

        return "index"
    }
}
