
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {
	var random = scala.util.Random
	val httpProtocol = http
		.baseUrl("http://computer-database.gatling.io")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("pl,en-US;q=0.7,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0")


	object Search {
		val search = exec(http("Home")
			.get("/"))
			.feed(csv("search.csv").random)
			.exec(http("Search_for_computer")
			.get("/computers?f=${searchCriterion}")
			.check(css("a:contains('${searchComputerName}')"))
			)
		
	}
	object Browse {
		val browse = 
		repeat(5 ,"i") {
		exec(http("Click_next")
			.get("/computers?p=${i}"))
			.pause(1,3)
		}
	}

	object Computer {
		val openForm = exec(
			http("Open_new_computer_form")
			.get("/computers/new"))
		
		val addRandom = exec(
			http("Add_new_computer")
			.post("/computers")
			.check(status.is(session => 200)) 
			.formParam("name", random.alphanumeric.take(20).mkString)
			.formParam("introduced", "2222-12-22")
			.formParam("discontinued", "3333-11-11")
			.formParam("company", random.nextInt(43))
		)
	}


	val scn1 = scenario("Browse_and_search")
		.exec(Search.search,
		      Browse.browse)	
	val scn2 = scenario("Add")
		.repeat(8) {
				exec(
					Computer.openForm,
					Computer.addRandom)
				.pause(1,3)
		 }
	setUp(
		scn1.inject(atOnceUsers(5)),
		scn2.inject(rampUsers(5) during (15 seconds))		
		)
		.protocols(httpProtocol)
		.assertions(global.responseTime.max.lt(500))
		.assertions(forAll.failedRequests.percent.lte(2))
}