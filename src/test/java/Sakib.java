import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.openqa.selenium.By.xpath;

public class Sakib {
    //private static final Logger logger = LogManager.getLogger(Login.class);
    private static final Logger logger = LogManager.getLogger(Sakib.class);
    WebDriver driver;
    ExcelUtil excelUtil;
    ExtentReports extent;
    ExtentSparkReporter htmlReporter;
    ExtentTest test;

    @BeforeSuite
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Initialize ExcelUtil
        String filePath = "src/main/resources/sheet1.xlsx";
        excelUtil = new ExcelUtil(filePath, "sheet1");

        // Initialize ExtentReports
        htmlReporter = new ExtentSparkReporter("Sakib.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @Test
    public void testLogin() {
        test = extent.createTest("Login Test");
        driver.get("https://mybdjobs.bdjobs.com/mybdjobs/signin.asp");
        test.info("Navigated to login page.");

        for (int i = 1; i <= excelUtil.getRowCount(); i++) {
            String username = excelUtil.getCellData(i, 0);
            String password = excelUtil.getCellData(i, 1);
            try {
                login(username, password);
                test.info("Attempted login with username: " + username);
                logout();
                test.info("Logged out successfully.");
            } catch (Exception e) {
                logger.error("Login failed for username: " + username + ". Error: " + e.getMessage());
                test.fail("Login failed for username: " + username + ". Error: " + e.getMessage());
            }
            // Navigate back to the login page for the next iteration
            driver.get("https://mybdjobs.bdjobs.com/mybdjobs/signin.asp");
            test.info("Navigated back to login page for the next iteration.");
        }
    }

    public void login(String username, String password) throws InterruptedException {
        // Input username
        By userName = xpath("//input[@id='TXTUSERNAME']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(userName)).sendKeys(username);
        Thread.sleep(1000);

        // Click Continue
        By continueButton = xpath("//input[@value='Continue']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(continueButton)).click();

        // Check for incorrect username error
        By usernameError = By.xpath("//span[@id='errorMessage' and contains(text(), \"Couldn't find your Bdjobs account.Click on Create Account button to create Bdjobs account.  \")]");
        if (isElementDisplayed(usernameError)) {
            String actualErrorMessage = driver.findElement(usernameError).getText();
            String expectedErrorMessage = "Wrong Username";
            try {
                Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Username error message mismatch");
            } catch (AssertionError e) {
                logger.error("Assertion failed for username: " + username + ". Error: " + e.getMessage());
                test.fail("Assertion failed for username: " + username + ". Error: " + e.getMessage());
            }
            return;
        }

        // Input password
        By passwordField = By.cssSelector("#TXTPASS");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        Thread.sleep(3000);

        // Click on finalSignIn
        By finalSignIn = xpath("//input[@value='Sign in']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(finalSignIn)).click();
        Thread.sleep(2000);

        // Check for incorrect password error
        By passwordError = By.xpath("//span[@id='errorMessage' and contains(text(), 'Wrong password. Try again or click Forgot password to reset it.')]");
        if (isElementDisplayed(passwordError)) {
            String actualErrorMessage = driver.findElement(passwordError).getText();
            String expectedErrorMessage = "Incorrect password";
            try {
                Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Password error message mismatch");
            } catch (AssertionError e) {
                logger.error("Assertion failed for password with username: " + username + ". Error: " + e.getMessage());
                test.fail("Assertion failed for password with username: " + username + ". Error: " + e.getMessage());
            }
        }
    }


    public boolean isElementDisplayed(By by) {
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public void logout() throws InterruptedException {
        // Click on UserDetailsDropdown
        By userdetailsDropdown = xpath("//a[@id='userDetailsDropdown']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(userdetailsDropdown)).click();
        Thread.sleep(5000);

        // Click on Logout
        By logout = xpath("//a[@class='btn-signout']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(logout)).click();
    }

    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        // Flush the extent report
        extent.flush();
    }
}
