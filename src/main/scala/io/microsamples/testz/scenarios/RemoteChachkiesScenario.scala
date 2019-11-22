package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object RemoteChachkiesScenario {

  val remoteChachkiesScenarioPath = http("get some cool remote chachkies")
    .get("/remote-chachkies")
    .check(status is 200)

  val chachkiesRoot = scenario("Get Remote Chachkies")
    .exec(remoteChachkiesScenarioPath)
}
