package io.microsamples.testz.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.microsamples.gatlingrunner.load.GatlingContext
import io.microsamples.testz.scenarios.{GetRequestScenario, PostRequestScenario}
import io.microsamples.testz.util.{Environment, Headers}

import scala.concurrent.duration._
import scala.language.postfixOps

class PostRequestSimulation extends Simulation {
  val httpConf: HttpProtocolBuilder = http.baseUrl(GatlingContext.INSTANCE.baseUrl)
    .headers(Headers.commonHeader)

  val postRequestScenarios = List(
    PostRequestScenario.postRequestScenario.inject(
      constantUsersPerSec(GatlingContext.INSTANCE.constantUsersPerSecond) during (GatlingContext.INSTANCE.constantUsersPerSecondDuration seconds),
      rampUsersPerSec(GatlingContext.INSTANCE.rampUsersPerSecondMinimum) to GatlingContext.INSTANCE.rampUsersPerSecondMaximum during (GatlingContext.INSTANCE.rampUsersPerSecondDuration seconds)
    )
  )

  val constantUsersPerMinuteDuration: Int = GatlingContext.INSTANCE.constantUsersPerSecondDuration * 60

  setUp(postRequestScenarios)
    .protocols(httpConf)
    .maxDuration((constantUsersPerMinuteDuration + 1) minutes)

    .assertions(
      global.responseTime.max.lt(Environment.maxResponseTime.toInt)
    )

}
