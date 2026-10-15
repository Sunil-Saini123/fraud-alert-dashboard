package com.example.fraudalert.selenium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

/**
 * Starts the application on a random port with an in-memory database,
 * opens a browser for every test and offers small helper methods.
 * Run with: mvn test -Pselenium   (add -Dheadless=false to watch the browser,
 * -Dbrowser=chrome to use Chrome instead of Edge).
 */
@Tag("selenium")
@ExtendWith(ScreenshotOnFailureExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:seleniumdb",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        })
public abstract class BaseSeleniumTest {

    @Value("${local.server.port}")
    private int port;

    private WebDriver driver;

    private Path profileDir;

    @BeforeEach
    void startBrowser() throws IOException {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        String browser = System.getProperty("browser", "edge");
        // A fresh, temporary browser profile works for any Windows account, including the Jenkins service
        profileDir = Files.createTempDirectory("selenium-profile-");
        String profileArg = "--user-data-dir=" + profileDir.toAbsolutePath();
        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(profileArg, "--no-first-run", "--no-default-browser-check",
                    "--disable-dev-shm-usage", "--window-size=1366,900");
            if (headless) {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-gpu");
            }
            driver = new ChromeDriver(options);
        } else {
            EdgeOptions options = new EdgeOptions();
            options.addArguments(profileArg, "--no-first-run", "--no-default-browser-check",
                    "--disable-dev-shm-usage", "--window-size=1366,900");
            if (headless) {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-gpu");
            }
            driver = new EdgeDriver(options);
        }
        // Authenticate before any test interacts with protected pages
        login("admin", "admin123");
    }

    /**
     * Navigates to the login page, fills the credentials and submits the form.
     * Waits until the browser leaves the /login URL before returning.
     */
    void login(String username, String password) {
        open("/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        // Wait until we are redirected away from the login page
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/login")));
    }

    @AfterEach
    void stopBrowser() {
        if (driver != null) {
            driver.quit();
        }
        try {
            FileSystemUtils.deleteRecursively(profileDir);
        } catch (IOException | RuntimeException ignored) {
            // the temporary profile folder is cleaned up on a best-effort basis
        }
    }

    WebDriver getDriver() {
        return driver;
    }

    void open(String path) {
        driver.get("http://localhost:" + port + path);
    }

    /** A transaction ID that is unique in every test run. */
    String uniqueId() {
        return "TXN-" + System.nanoTime();
    }

    void type(String id, String value) {
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(value);
    }

    void choose(String id, String value) {
        new Select(driver.findElement(By.id(id))).selectByValue(value);
    }

    String text(String id) {
        return driver.findElement(By.id(id)).getText();
    }

    boolean exists(String id) {
        return !driver.findElements(By.id(id)).isEmpty();
    }

    /**
     * Clicks a button or link that loads a new page and waits until the new page has loaded,
     * so the test never reads the old page by mistake.
     */
    void clickAndWait(String id) {
        WebElement oldPage = driver.findElement(By.tagName("html"));
        driver.findElement(By.id(id)).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.stalenessOf(oldPage));
        wait.until((ExpectedCondition<Boolean>) d ->
                "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    /** Reads the number shown on a dashboard indicator, e.g. "NEW". */
    int indicator(String name) {
        return Integer.parseInt(
                driver.findElement(By.cssSelector("#indicator-" + name + " .count")).getText().trim());
    }

    /** Fills the alert form and saves it. dueDate is optional (format yyyy-MM-dd). */
    void addAlert(String transactionId, String customer, String amount,
                  String risk, String status, String dueDate) {
        open("/alerts/new");
        type("transactionId", transactionId);
        type("customerName", customer);
        type("amount", amount);
        choose("riskLevel", risk);
        choose("status", status);
        if (dueDate != null) {
            WebElement due = driver.findElement(By.id("dueDate"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", due, dueDate);
        }
        clickAndWait("save-button");
    }
}
