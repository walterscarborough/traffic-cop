package com.walterscarborough.trafficcop.reports

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ReportsSettings {

    @Value(REPORTS_DIR_PATH)
    private lateinit var reportsDir: String

    fun getReportsDir(): String {
        return reportsDir
    }
}
