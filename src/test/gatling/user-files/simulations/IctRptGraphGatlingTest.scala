import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the IctRptGraph entity.
 */
class IctRptGraphGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8080"""

    val httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources // Silence all resources like css or css so they don't clutter the results

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the IctRptGraph entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        ).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJson
        .check(header("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(2)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all ictRptGraphs")
            .get("/api/ict-rpt-graphs")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new ictRptGraph")
            .post("/api/ict-rpt-graphs")
            .headers(headers_http_authenticated)
            .body(StringBody("""{
                "id":null
                , "timeType":null
                , "prdId":null
                , "kpiId":null
                , "kpiCode":"SAMPLE_TEXT"
                , "kpiName":"SAMPLE_TEXT"
                , "objCode":"SAMPLE_TEXT"
                , "objName":"SAMPLE_TEXT"
                , "parentCode":"SAMPLE_TEXT"
                , "parentName":"SAMPLE_TEXT"
                , "inputLevel":null
                , "valPlan":null
                , "valPlanMon":null
                , "valPlanQuar":null
                , "valPlanYear":null
                , "val":null
                , "valMon":null
                , "valQuar":null
                , "valYear":null
                , "valLastMon":null
                , "valLastQuar":null
                , "valLastYear":null
                , "valDelta":null
                , "valDeltaMon":null
                , "valDeltaQuar":null
                , "valDeltaYear":null
                , "percentPlan":null
                , "percentPlanMon":null
                , "percentPlanQuar":null
                , "percentPlanYear":null
                , "percentGrow":null
                , "percentGrowMon":null
                , "percentGrowQuar":null
                , "percentGrowYear":null
                , "colorAlarm":"SAMPLE_TEXT"
                , "domainCode":"SAMPLE_TEXT"
                }""")).asJson
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_ictRptGraph_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created ictRptGraph")
                .get("${new_ictRptGraph_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created ictRptGraph")
            .delete("${new_ictRptGraph_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) during (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
