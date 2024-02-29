package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import utils.StringUtils
class PutDataClass extends Simulation {
    val strginUtils = new StringUtils()
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
  				"/" + keys(scala.util.Random.nextInt(keys.length))
			})
		 	.body(StringBody( _ => """{ "data1": """" + strginUtils.generateRandomString(20) 
			 					+ """", "data2": """" + strginUtils.generateRandomString(20) + """" }""")).asJson
		 )  
	}
}