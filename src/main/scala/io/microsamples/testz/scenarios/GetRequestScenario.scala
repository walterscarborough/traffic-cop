package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object GetRequestScenario {

  val getRequestScenarioCommand = http("create get request")
    .get("/remote-chachkies")
    .check(status is 200)

  val getRequestScenario = scenario("execute get request")
    .exec(getRequestScenarioCommand)
}
