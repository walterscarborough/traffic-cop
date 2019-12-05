package io.microsamples.testz

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.microsamples.gatlingrunner.load.{GatlingContext, HttpMethod}
import io.microsamples.testz.scenarios.{GetRequestScenario, PostRequestScenario}

import scala.concurrent.duration._
import scala.language.postfixOps


object ScenarioFactory {
  def createScenarios(): List[PopulationBuilder] = {
    return GatlingContext.INSTANCE.httpMethod match {
      case HttpMethod.GET => {
        return List(
          GetRequestScenario.getRequestScenario.inject(
            constantUsersPerSec(GatlingContext.INSTANCE.constantUsersPerSecond) during (GatlingContext.INSTANCE.constantUsersPerSecondDuration seconds),
            rampUsersPerSec(GatlingContext.INSTANCE.rampUsersPerSecondMinimum) to GatlingContext.INSTANCE.rampUsersPerSecondMaximum during (GatlingContext.INSTANCE.rampUsersPerSecondDuration seconds)
          )
        )
      }

      case HttpMethod.POST => {
        return List(
          PostRequestScenario.postRequestScenario.inject(
            constantUsersPerSec(GatlingContext.INSTANCE.constantUsersPerSecond) during (GatlingContext.INSTANCE.constantUsersPerSecondDuration seconds),
            rampUsersPerSec(GatlingContext.INSTANCE.rampUsersPerSecondMinimum) to GatlingContext.INSTANCE.rampUsersPerSecondMaximum during (GatlingContext.INSTANCE.rampUsersPerSecondDuration seconds)
          )
        )
      }
    }
  }
}
