package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import utils.StringUtils

class PostDataClass extends Simulation {
   val strginUtils = new StringUtils()

   object PostData {
  		val postData = exec(
			http("Post simple data")
			.post("/data")
			.body(StringBody( _ => """{ "data1": """" + strginUtils.generateRandomString(20) 
								+ """", "data2": """" + strginUtils.generateRandomString(20) + """" }""")).asJson
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
				.post("/data")
				.body(RawFileBody("data_json.json"))
				.check(status.is(session => 200))
		)
		val postBigData = exec(
				http("Post big data")
				.post("/data")
				.body(StringBody( _ => """{ "data1": """" + strginUtils.generateRandomString(3000) 
									+ """", "data2": """" + strginUtils.generateRandomString(3000) + """" }""")).asJson
				.check(status.is(session => 200))
				.check(bodyString.is("OK"))
			)
		}
}