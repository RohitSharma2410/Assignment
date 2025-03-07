package engine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public class Hooks {

	public static RequestSpecification requestSpec = null;
	public static ResponseSpecBuilder res = null;

	@Before
	public static void before() {
		try {
//Setting up ResponseSpecBuilder to test response at run time in step definition class
			res = new ResponseSpecBuilder();
			res.expectStatusCode(Matchers.lessThan(500));
//Added custom http client config and Connection config and bind it with RestAssured to handle to timeout cases			
			
			Map<String, Object> addmap = new HashMap<>();
			addmap.put("http.connection.timeout", 10000);
			HttpClientConfig httpConfig = HttpClientConfig.httpClientConfig().addParams(addmap);
			Map<String, Object> config = new HashMap<>();
			config.put("http.connection.timeout", 10000);
			ConnectionConfig conconfig = ConnectionConfig.connectionConfig()
					.closeIdleConnectionsAfterEachResponseAfter(10000, TimeUnit.SECONDS);
			
	
			RestAssuredConfig con = RestAssured.config.httpClient(httpConfig).connectionConfig(conconfig);
	//Added filters to log request to LogsFiles
			FileOutputStream requestLogFile = new FileOutputStream(
					System.getProperty("user.dir") + "/logs/requestLogs.log", true); // 'true' for appending
			FileOutputStream responseLogFile = new FileOutputStream(
					System.getProperty("user.dir") + "/logs/responseLogs.log", true); // 'true' for appending
		
	//Built base request specification to be used further for request		
			requestSpec = RestAssured.given().config(con).contentType("application/json")
					.filter(RequestLoggingFilter.logRequestTo(new PrintStream(requestLogFile)))
					.filter(ResponseLoggingFilter.logResponseTo(new PrintStream(responseLogFile)))
					.urlEncodingEnabled(false).log().all();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
