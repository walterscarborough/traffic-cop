package com.walterscarborough.trafficcop.gatlingrunner.simulation

import com.walterscarborough.trafficcop.context.GatlingContext
import com.walterscarborough.trafficcop.gatlingrunner.scenarios.ScenarioFactory
import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class GeneralSimulation extends Simulation {
  val httpConf: HttpProtocolBuilder = http.baseUrl(GatlingContext.INSTANCE.getBaseUrl)
    .headers(
      Map(
        "Accept" -> "application/json, text/javascript, */*; q=0.01",
        "Accept-Charset" -> "ISO-8859-1,utf-8;q=0.7,*;q=0.7",
        "Content-Type" -> "application/json"
      )
    )

  val scenarios: List[PopulationBuilder] = ScenarioFactory.createScenarios()

  val constantUsersPerMinuteDuration: Int = GatlingContext.INSTANCE.getConstantUsersPerSecondDuration * 60

  val maxResponseTimeMilliseconds = 10000

  setUp(scenarios)
    .protocols(httpConf)
    .maxDuration((constantUsersPerMinuteDuration + 1) minutes)
    .assertions(
      global.responseTime.max.lt(maxResponseTimeMilliseconds)
    )
}
