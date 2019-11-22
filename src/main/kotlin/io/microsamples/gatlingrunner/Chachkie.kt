package io.microsamples.gatlingrunner

import java.time.Instant

data class Chachkie(
    val age: Int,
    val id: String,
    val name: String,
    val description: String,
    val `when`: Instant
)
