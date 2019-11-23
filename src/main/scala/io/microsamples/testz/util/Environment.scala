package io.microsamples.testz.util

object Environment {
  val postRequestUrl = scala.util.Properties.envOrElse("baseURL", "https://en8rkxo28maj.x.pipedream.net/")
  val users = scala.util.Properties.envOrElse("numberOfUsers", "5")
  val maxResponseTime = scala.util.Properties.envOrElse("maxResponseTime", "10000") // in milliseconds

}
