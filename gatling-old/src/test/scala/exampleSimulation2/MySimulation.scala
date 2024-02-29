
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import requests.GetDataClass
import requests.PostDataClass
import requests.PutDataClass

class MySimulation extends Simulation {
	var getData = new GetDataClass().GetData
	var postData = new PostDataClass().PostData
	var putData = new PutDataClass().PutData
		
	val httpProtocol = http
		.baseUrl("http://localhost:3000")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("pl,en-US;q=0.7,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0")
	
	val getScenario = scenario("GetData")
		.exec(getData.getData,getData.getApiData)	
	
	val postScenario = scenario("PostData")
		.repeat(8) {
			exec(postData.postData)
			.pause(1,3)
		 }
	val postFromFileScenario = scenario("PostDataFromFile")
			.exec(postData.postDataFromFile)
			.pause(1,3)
		 
	val postFromJsonScenario = scenario("PostDataFromJson")
			.exec(postData.postDataFromJson)
			.pause(1,3)
		
	val postBigDataScenario = scenario("PostBigData")
		.repeat(3) {
			 exec(postData.postBigData)
			.pause(1,3)
		 }
	val putScenario = scenario("PutData")
		.exec(putData.getKeys,
			  putData.putData)
		.pause(1,3)

	setUp(
		getScenario.inject(atOnceUsers(10)),
		postScenario.inject(rampUsers(5) during (3 seconds)),
		postBigDataScenario.inject(rampUsers(5) during (3 seconds)),
		postFromFileScenario.inject(rampUsers(5) during (3 seconds)),
		postFromJsonScenario.inject(rampUsers(5) during (3 seconds)),
		putScenario.inject(rampUsers(5) during (3 seconds))			
		)
		.protocols(httpProtocol)
		.assertions(global.responseTime.max.lt(500))
		.assertions(forAll.failedRequests.percent.lte(2))
}