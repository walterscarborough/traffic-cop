package io.microsamples.testz

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.microsamples.gatlingrunner.load.{GatlingContext, HttpMethod}
import io.microsamples.testz.scenarios.{DeleteRequestScenario, GetRequestScenario, PostRequestScenario, PutRequestScenario}

import scala.concurrent.duration._
import scala.language.postfixOps


object ScenarioFactory {
  def createScenarios(): List[PopulationBuilder] = {
    return GatlingContext.INSTANCE.getHttpMethod match {

      case HttpMethod.GET => {
        return List(
          GetRequestScenario.getRequestScenario.inject(
            constantUsersPerSec(GatlingContext.INSTANCE.getConstantUsersPerSecond) during (GatlingContext.INSTANCE.getConstantUsersPerSecondDuration seconds),
            rampUsersPerSec(GatlingContext.INSTANCE.getRampUsersPerSecondMinimum) to GatlingContext.INSTANCE.getRampUsersPerSecondMaximum during (GatlingContext.INSTANCE.getRampUsersPerSecondDuration seconds)
          )
        )
      }

      case HttpMethod.PUT => {
        return List(
          PutRequestScenario.putRequestScenario.inject(
            constantUsersPerSec(GatlingContext.INSTANCE.getConstantUsersPerSecond) during (GatlingContext.INSTANCE.getConstantUsersPerSecondDuration seconds),
            rampUsersPerSec(GatlingContext.INSTANCE.getRampUsersPerSecondMinimum) to GatlingContext.INSTANCE.getRampUsersPerSecondMaximum during (GatlingContext.INSTANCE.getRampUsersPerSecondDuration seconds)
          )
        )
      }


      case HttpMethod.POST => {
        return List(
          PostRequestScenario.postRequestScenario.inject(
            constantUsersPerSec(GatlingContext.INSTANCE.getConstantUsersPerSecond) during (GatlingContext.INSTANCE.getConstantUsersPerSecondDuration seconds),
            rampUsersPerSec(GatlingContext.INSTANCE.getRampUsersPerSecondMinimum) to GatlingContext.INSTANCE.getRampUsersPerSecondMaximum during (GatlingContext.INSTANCE.getRampUsersPerSecondDuration seconds)
          )
        )
      }

      case HttpMethod.DELETE => {
        return List(
          DeleteRequestScenario.deleteRequestScenario.inject(
            constantUsersPerSec(GatlingContext.INSTANCE.getConstantUsersPerSecond) during (GatlingContext.INSTANCE.getConstantUsersPerSecondDuration seconds),
            rampUsersPerSec(GatlingContext.INSTANCE.getRampUsersPerSecondMinimum) to GatlingContext.INSTANCE.getRampUsersPerSecondMaximum during (GatlingContext.INSTANCE.getRampUsersPerSecondDuration seconds)
          )
        )
      }
    }
  }
}
