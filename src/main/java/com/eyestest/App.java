package com.eyestest;

import com.applitools.eyes.TestResultContainer;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class App {
    private static ClassicRunner runner = new ClassicRunner();

    public static void main(String[] args) throws MalformedURLException
    {
        configure((eyes, wd) ->
        {
            eyes.check(Target.window());
        });
        afterTestSuite();
        System.exit(0);
    }

    private static void configure(BiConsumer<Eyes, WebDriver> configured) throws MalformedURLException
    {
        RemoteWebDriver driver = null;
        Eyes eyes = null;

        MutableCapabilities capabilities = new MutableCapabilities();

        capabilities.setCapability("acceptInsecureCerts", "true");
        capabilities.setCapability("appium:automationName", "XCUITest");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("appium:platformVersion", "16.0");
        capabilities.setCapability("appium:deviceName", "iPhone Simulator");
        capabilities.setCapability("appium:screenshotWaitTimeout", "20");
        capabilities.setCapability("appium:waitForAppScript", "$.delay(5000); $.acceptAlert();");
        capabilities.setCapability("appium:app", "https://github.com/vividus-framework/vividus-test-app/releases/download/0.2.0/vividus-test-app-ios-0.2.0.zip");

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("name", "VisualTestingSteps");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("appiumVersion", "2.0.0");

        capabilities.setCapability("sauce:options", sauceOptions);

        driver = new RemoteWebDriver(new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub"), capabilities);
        try {

            eyes = new Eyes(runner);

            eyes.setConfiguration(new DefaultConfigurationProvider().get());
            eyes.setApiKey(System.getenv("API_KEY"));
            eyes.setServerUrl(System.getenv("SERVER_URL"));

            eyes.open(driver, "selenium-eyes5-region-check", "selenium-eyes5-region-check-execution");

            configured.accept(eyes, driver);
        }
        finally {
            try {
                eyes.closeAsync();
            } catch (Exception e) {
                eyes.abortAsync();
            }
            driver.quit();
        }
    }

    private static void afterTestSuite() {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        for (TestResultContainer result : allTestResults) {
            handleTestResults(result);
        }
    }

    static void handleTestResults(TestResultContainer summary) {
        Throwable ex = summary.getException();
        if (ex != null) {
            System.out.printf("System error occured while checking target.\n");
        }
        TestResults result = summary.getTestResults();
        if (result == null) {
            System.out.printf("No test results information available\n");
        } else {
            System.out.printf("URL = %s,\n AppName = %s, testname = %s, Browser = %s,OS = %s, viewport = %dx%d, matched = %d,mismatched = %d, missing = %d,aborted = %s\n",
                    result.getUrl(),
                    result.getAppName(),
                    result.getName(),
                    result.getHostApp(),
                    result.getHostOS(),
                    result.getHostDisplaySize().getWidth(),
                    result.getHostDisplaySize().getHeight(),
                    result.getMatches(),
                    result.getMismatches(),
                    result.getMissing(),
                    (result.isAborted() ? "aborted" : "completed OK"));
        }
    }

}
