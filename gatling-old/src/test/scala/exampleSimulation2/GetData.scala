package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class GetDataClass extends Simulation {
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
            val getApiData = exec(http("get API data")
                .get("/data")
                .check(status.is(session => 200))
                )
        }
}