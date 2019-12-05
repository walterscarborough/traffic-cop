package io.microsamples.testz

import io.gatling.app.Gatling

object GatlingWrapper {

  def startGatling(args: Array[String]): Unit = {
    Gatling.main(args)
  }
}
