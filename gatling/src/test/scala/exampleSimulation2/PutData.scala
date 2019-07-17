package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PutDataClass extends Simulation {
    var random = scala.util.Random
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
		 	.body(StringBody( _ => """{ "data1": """" + random.alphanumeric.take(20).mkString 
			 					+ """", "data2": """" + random.alphanumeric.take(20).mkString + """" }""")).asJson
		 )  
	}
}