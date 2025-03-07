package engine;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = { "features" }, glue = { "engine", "engine.Hooks" }, tags = "@Base"

)
public class Runner extends AbstractTestNGCucumberTests {
	public static int totalThreads = -1;

	@Override
	@DataProvider(parallel = false)
	public Object[][] scenarios() {

		return super.scenarios();
	}

}
