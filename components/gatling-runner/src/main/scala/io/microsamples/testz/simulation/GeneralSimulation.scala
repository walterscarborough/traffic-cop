package io.microsamples.testz.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.microsamples.gatlingrunner.load.GatlingContext
import io.microsamples.testz.ScenarioFactory
import io.microsamples.testz.util.{Environment, Headers}

import scala.concurrent.duration._
import scala.language.postfixOps

class GeneralSimulation extends Simulation {
  val httpConf: HttpProtocolBuilder = http.baseUrl(GatlingContext.INSTANCE.baseUrl)
    .headers(Headers.commonHeader)

  val scenarios: List[PopulationBuilder] = ScenarioFactory.createScenarios()

  val constantUsersPerMinuteDuration: Int = GatlingContext.INSTANCE.constantUsersPerSecondDuration * 60

  setUp(scenarios)
    .protocols(httpConf)
    .maxDuration((constantUsersPerMinuteDuration + 1) minutes)

    .assertions(
      global.responseTime.max.lt(Environment.maxResponseTime.toInt)
    )
}
