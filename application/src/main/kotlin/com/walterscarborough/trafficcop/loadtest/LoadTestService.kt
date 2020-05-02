package com.walterscarborough.trafficcop.loadtest

import com.walterscarborough.trafficcop.reports.ReportsSettings
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class LoadTestService(
    private val service: AsyncService,
    private val reportsSettings: ReportsSettings
) {

    fun executeGatlingScenario(runLoadRequest: RunLoadRequest): ResponseEntity<String> {
        val simulationClass = "com.walterscarborough.trafficcop.gatlingrunner.simulation.GeneralSimulation"
        val args = arrayOf(
            "-s", simulationClass,
            "-rf", reportsSettings.getReportsDir()
        )
        runTestNoExit(args)
        return ResponseEntity.ok("Report Scheduled...  Check /reports in few minutes")
    }

    private fun runTestNoExit(args: Array<String>) {
        val secManager = MySecurityManager()
        System.setSecurityManager(secManager)
        service.doWork(
            Runnable {
                try {
                    com.walterscarborough.trafficcop.gatlingrunner.GatlingWrapper.startGatling(args)
                } catch (securityException: SecurityException) {
                }
            }
        )
    }
}
