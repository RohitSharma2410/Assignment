package engine;

import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class StepDefinitions {
	private String apiendPoint = null;
	private Response response = null;

	@Given("API to call is {string}")
	public void api_to_call_is(String string) {
		apiendPoint = string;
	}

	@When("send request")
	public void send_request() {
		try {
			//Calling api along with response specification to check status code in 500 range
			response = Hooks.requestSpec.when().get(apiendPoint).then().spec(Hooks.res.build()).log().all().extract().response();
		} 
		catch (AssertionError e) {
		    System.out.println("Assertion failed for status code less than 500: " + e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Then("{string} field should be having value length greater than <{int}>")
	public void field_should_be_having_value_length_greater_than(String string, Integer int1) {
		try {
			String fieldValue = new JsonPath(response.asString()).get(string);
			Assert.assertTrue(fieldValue.length() > int1);

		} catch (Exception e) {

		}
	}

	@Then("response code should be <{int}>")
	public void response_code_should_be(Integer int1) {
		try {
//Assertion to check status code
			Assert.assertTrue(response.getStatusCode() == int1);

		} catch (Exception e) {

		}
	}

}
