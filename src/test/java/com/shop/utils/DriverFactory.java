package com.shop.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void quitDriver() {
        WebDriver driverInstance = driver.get();
        if (driverInstance != null) {
            driverInstance.quit();
            driver.remove();
        }
    }

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (isCI()) {
            options.addArguments("--headless=new");
        }
        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        return chromeDriver;
    }

    private static boolean isCI() {
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }
}
