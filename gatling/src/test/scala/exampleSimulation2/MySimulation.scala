
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
			.check(status.is(session => 200))
			.check(bodyString.transform(_.size > 2999).is(true))
			.check(bodyString.saveAs( "RESPONSE_DATA" )) 
			)
			.exec( session => {
				println( session( "RESPONSE_DATA" ).as[String] )
				session
			})  
	}
	object PutData {
		val getKeys = exec(http("Get Keys")
			.get("/data")
			.check(status.is(session => 200))
			.check(regex( "(?<=key:).*?(?=\")").findAll.saveAs( "KEYS" )) 
			)
			.exec( session => {
				println( session( "KEYS" ).as[String] )
				session
			})
		val putData = exec(http("Put Data")
		 	.put(session => {
				var keys = session( "KEYS" ).as[Seq[String]]
  				"/" + keys(random.nextInt(keys.length))
			})
		 	.body(StringBody( _ => """{ "data1": """" + random.alphanumeric.take(20).mkString + """", "data2": """" + random.alphanumeric.take(20).mkString + """" }""")).asJson
		 )  
	}
	
	object PostData {
		val postData = exec(
			http("Post example data")
			.post("/data")
			.body(StringBody( _ => """{ "data1": """" + random.alphanumeric.take(20).mkString + """", "data2": """" + random.alphanumeric.take(20).mkString + """" }""")).asJson
			.check(status.is(session => 200))
		)
		val postBigData = exec(
			http("Post example data")
			.post("/data")
			.body(StringBody( _ => """{ "data1": """" + random.alphanumeric.take(3000).mkString + """", "data2": """" + random.alphanumeric.take(3000).mkString + """" }""")).asJson
			.check(status.is(session => 200))
			.check(bodyString.is("OK"))
		)
	}
	val getScenario = scenario("GetData")
		.exec(GetData.getData)	
	
	val postScenario = scenario("PostData")
		.repeat(8) {
				exec(
					PostData.postData)
				.pause(1,3)
		 }
	val postBigDataScenario = scenario("PostBigData")
		.repeat(8) {
				exec(
					PostData.postBigData)
				.pause(1,3)
		 }
	val putScenario = scenario("PutData")
		.exec(PutData.getKeys,
			  PutData.putData)
		.pause(1,3)

	setUp(
		getScenario.inject(atOnceUsers(10)),
		postScenario.inject(rampUsers(5) during (3 seconds)),
		postBigDataScenario.inject(rampUsers(5) during (3 seconds)),
		putScenario.inject(rampUsers(5) during (3 seconds))			
		)
		.protocols(httpProtocol)
		.assertions(global.responseTime.max.lt(500))
		.assertions(forAll.failedRequests.percent.lte(2))
}