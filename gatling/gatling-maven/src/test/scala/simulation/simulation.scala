package gatling.maven.simulation 

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

class Example1 extends Simulation {
 var random = scala.util.Random
  
 val httpProtocol = http
    .baseUrl("http://192.168.50.241:8080") 
 val scn = scenario("GetSimulation")
    .exec(http("Get Random String") 
      .get("/randStr")
      .check(status.is(200)))
    .pause(2)
  
 val scn1 = scenario("PostSimulation")
    .exec(http("Post Random String")
      .post("/postData")
      .header("content-type", "application/json")
      .body(StringBody(s"""{"field1": "${random.alphanumeric.take(20).mkString}","field2": "${random.alphanumeric.take(20).mkString}"}""")).asJson
      .check(status.is(200)))
    .pause(2)  
    
    setUp( 
    scn.inject(rampUsers(10) during(30 seconds)),
    scn1.inject(constantConcurrentUsers(10) during(30 seconds))
  ).protocols(httpProtocol)
}

// TODO random.alphanumeric.take(20).mkString runs only once
// TODO not found errros in vscodium