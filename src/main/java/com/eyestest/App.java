package com.eyestest;

import java.util.function.BiConsumer;

import com.applitools.connectivity.ServerConnector;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.ClassicRunner0;
import com.applitools.eyes.selenium.SeleniumEyes;
import com.applitools.eyes.selenium.wrappers.EyesSeleniumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResultContainer;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;

import io.github.bonigarcia.wdm.WebDriverManager;

public class App {
    private static VisualGridRunner runner = new VisualGridRunner(2);

    public static void main(String[] args) {
        configure((eyes, wd) ->
        {
            wd.get(System.getenv("SITE_URL"));
            eyes.checkWindow();
        });
        afterTestSuite();
        System.exit(0);
    }

    private static void configure(BiConsumer<SeleniumEyes, WebDriver> configured) {
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = null;
        SeleniumEyes eyes = null;
        try {
            eyes = new SeleniumEyes(new DefaultConfigurationProvider(), new ClassicRunner0());
            eyes.setServerConnector(new ServerConnector());
            eyes.setApiKey(System.getenv("API_KEY"));
            eyes.setServerUrl(System.getenv("SERVER_URL"));
            eyes.setLogHandler(new StdoutLogHandler(true));

            driver = new ChromeDriver();
            EyesSeleniumDriver eyesSeleniumDriver = new EyesSeleniumDriver(eyes.getLogger(), eyes, driver);

            eyes.open(eyesSeleniumDriver, "selenium-eyes5-session-open-issue", "eyes5-session-open-issue-execution",
                    new RectangleSize(1600, 1200));
            configured.accept(eyes, driver);
        }
        finally {
            eyes.closeAsync();
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
