# Steps
1. `git clone https://github.com/vkepin/eyes-frame-exception.git`
2. `export API_KEY=<key>`
3. `export SERVER_URL=<key>`
4. `export SITE_URL=<key>`
5. `./gradlew run`

# Error traces
```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "org.openqa.selenium.WebDriver$TargetLocator.defaultContent()" because the return value of "com.applitools.eyes.selenium.wrappers.EyesSeleniumDriver.switchTo()" is null```
