
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MySimulation extends Simulation {
	var random = scala.util.Random
	val httpProtocol = http
		.baseUrl("http://localhost:3000")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("pl,en-US;q=0.7,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0")


	object GetData {
		val getData = exec(http("get data")
			.get("/")
			.check(status.is(session => 200)))
		
	}
	
	object PostData {
		
		val postData = exec(
			http("Post example data")
			.post("/data")
			.body(StringBody(_ => """{ "data1": """" + random.alphanumeric.take(20).mkString + """", "data2": """" + random.alphanumeric.take(20).mkString + """" }""")).asJson
			.check(status.is(session => 200))
		)
		val postBigData = exec(
			http("Post example data")
			.post("/data")
			.body(StringBody(_ => """{ "data1": """" + random.alphanumeric.take(3000).mkString + """", "data2": """" + random.alphanumeric.take(3000).mkString + """" }""")).asJson
			.check(status.is(session => 200))
		)
	}


	val scn1 = scenario("GetData")
		.exec(GetData.getData)	
	val scn2 = scenario("PostData")
		.repeat(8) {
				exec(
					PostData.postData)
				.pause(1,3)
		 }
	val scn3 = scenario("PostBigData")
		.repeat(8) {
				exec(
					PostData.postBigData)
				.pause(1,3)
		 }
	setUp(
		scn1.inject(atOnceUsers(5)),
		scn2.inject(rampUsers(5) during (3 seconds)),
		scn3.inject(rampUsers(5) during (3 seconds))		
		)
		.protocols(httpProtocol)
		.assertions(global.responseTime.max.lt(500))
		.assertions(forAll.failedRequests.percent.lte(2))
}