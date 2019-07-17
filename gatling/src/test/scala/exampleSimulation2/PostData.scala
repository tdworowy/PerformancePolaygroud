package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PostDataClass extends Simulation {
   var random = scala.util.Random
   object PostData {
  		val postData = exec(
			http("Post simple data")
			.post("/data")
			.body(StringBody( _ => """{ "data1": """" + random.alphanumeric.take(20).mkString 
								+ """", "data2": """" + random.alphanumeric.take(20).mkString + """" }""")).asJson
			.check(status.is(session => 200))
		)
		val postDataFromFile = exec(
				feed(csv("data.csv").random)
				.exec(http("Post data from CSV")
				.post("/data")
				.body(StringBody("""{ "data1":"${data1}" ,"data2":"${data2}" }""")).asJson
				.check(status.is(session => 200))
		))
		val postDataFromJson = exec(
				http("Post data from Json")
				//.feed(jsonFile("data_json.json"))
				.post("/data")
				.body(RawFileBody("data_json.json"))//.asJSON
				.check(status.is(session => 200))
		)
		val postBigData = exec(
				http("Post big data")
				.post("/data")
				.body(StringBody( _ => """{ "data1": """" + random.alphanumeric.take(3000).mkString 
									+ """", "data2": """" + random.alphanumeric.take(3000).mkString + """" }""")).asJson
				.check(status.is(session => 200))
				.check(bodyString.is("OK"))
			)
		}
}