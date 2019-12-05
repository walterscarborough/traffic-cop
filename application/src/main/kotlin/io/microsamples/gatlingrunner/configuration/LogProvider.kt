package io.microsamples.gatlingrunner.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogProvider {
    companion object {
        fun getLogger(T:Class<Any>): Logger {
            return LoggerFactory.getLogger(T::class.java)
        }
    }
}
