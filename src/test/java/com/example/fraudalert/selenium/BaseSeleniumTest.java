package com.example.fraudalert.selenium;

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
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeEach
    void startBrowser() {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        String browser = System.getProperty("browser", "edge");
        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--window-size=1366,900");
            driver = new ChromeDriver(options);
        } else {
            EdgeOptions options = new EdgeOptions();
            if (headless) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--window-size=1366,900");
            driver = new EdgeDriver(options);
        }
    }

    @AfterEach
    void stopBrowser() {
        if (driver != null) {
            driver.quit();
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
        driver.findElement(By.id("save-button")).click();
    }
}
