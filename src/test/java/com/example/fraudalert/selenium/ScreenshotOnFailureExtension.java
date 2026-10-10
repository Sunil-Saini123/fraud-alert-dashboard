package com.example.fraudalert.selenium;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Saves a screenshot of the browser to target/screenshots when a test fails.
 * It runs right after the test method, before the browser is closed.
 */
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isEmpty()) {
            return;
        }
        Object testInstance = context.getRequiredTestInstance();
        if (!(testInstance instanceof BaseSeleniumTest base)) {
            return;
        }
        WebDriver driver = base.getDriver();
        if (!(driver instanceof TakesScreenshot camera)) {
            return;
        }
        File source = camera.getScreenshotAs(OutputType.FILE);
        Path folder = Path.of("target", "screenshots");
        Files.createDirectories(folder);
        String name = context.getRequiredTestClass().getSimpleName() + "_"
                + context.getRequiredTestMethod().getName() + ".png";
        Files.copy(source.toPath(), folder.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Failure screenshot saved: " + folder.resolve(name).toAbsolutePath());
    }
}
