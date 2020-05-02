package com.walterscarborough.trafficcop.configuration

class GatlingConfiguration {
    companion object {
        fun configureOutputDirectory() {
            System.setProperty("gatling.core.outputDirectoryBaseName", "loadTestReport")
        }
    }
}
