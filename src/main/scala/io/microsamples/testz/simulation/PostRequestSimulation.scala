package io.microsamples.testz.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.microsamples.testz.scenarios.{PostRequestScenario, GetRequestScenario}
import io.microsamples.testz.util.{Environment, Headers}

import scala.concurrent.duration._
import scala.language.postfixOps

class PostRequestSimulation extends Simulation {
  val httpConf = http.baseUrl(Environment.chachkiesBaseUrl)
    .headers(Headers.commonHeader)

  val postRequestScenarios = List(
      PostRequestScenario.postRequestScenario.inject(
       constantUsersPerSec(3) during (5 seconds)
    )
  )

  setUp(postRequestScenarios)
    .protocols(httpConf)
    .maxDuration(5 minutes)

    .assertions(
      global.responseTime.max.lt(Environment.maxResponseTime.toInt)
    )

}
