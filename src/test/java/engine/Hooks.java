package engine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.HttpRetryException;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hamcrest.Matchers;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.HttpClientConfig.HttpClientFactory;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.internal.RestAssuredHttpBuilderGroovyHelper;
import io.restassured.internal.http.HTTPBuilder;
import io.restassured.specification.RequestSpecification;

public class Hooks {

	public static RequestSpecification requestSpec = null;
	public static ResponseSpecBuilder res = null;

	@Before
	public static void before() {
		try {
		//Creating Request Config object
			RequestConfig requestConfig = RequestConfig.custom()
	                .setConnectTimeout(5000) 
	                .setSocketTimeout(5000) 
	                .build();

	        //Create http client object and passing request config object 
	        CloseableHttpClient httpClient = HttpClients.custom()
	                .setDefaultRequestConfig(requestConfig)
	                .setConnectionManager(new BasicHttpClientConnectionManager()) // Connection pooling
	                .build();
	        
	        //Create rest assured config object that can be passed to rest assured config method
	        RestAssuredConfig config = RestAssuredConfig.config()
	                .httpClient(HttpClientConfig.httpClientConfig()
	                        .setParam("http.client", httpClient) // Pass the custom HttpClient here
	                );
      
	     
	        

//Setting up ResponseSpecBuilder to test response at run time in step definition class
			res = new ResponseSpecBuilder();
			res.expectStatusCode(Matchers.lessThan(500));
//Added custom http client config and Connection config and bind it with RestAssured to handle to timeout cases			
			
		
		
			
	
	//Added filters to log request to LogsFiles
			FileOutputStream requestLogFile = new FileOutputStream(
					System.getProperty("user.dir") + "/logs/requestLogs.log", true); // 'true' for appending
			FileOutputStream responseLogFile = new FileOutputStream(
					System.getProperty("user.dir") + "/logs/responseLogs.log", true); // 'true' for appending
		
	//Built base request specification to be used further for request		
			requestSpec = RestAssured.given().config(config).contentType("application/json")
					.filter(RequestLoggingFilter.logRequestTo(new PrintStream(requestLogFile)))
					.filter(ResponseLoggingFilter.logResponseTo(new PrintStream(responseLogFile)))
					.urlEncodingEnabled(false).log().all();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
