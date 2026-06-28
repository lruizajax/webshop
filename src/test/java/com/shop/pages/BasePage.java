package com.shop.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForClickable(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getText(By locator) {
        return waitForElement(locator).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void waitForAjax() {
        try {
            wait.until(d -> {
                List<WebElement> loaders = d.findElements(
                        By.cssSelector(".ajax-loading-block, #ajax-loading, .loading-image, .ajax-loading"));
                boolean noVisibleLoader = loaders.stream().noneMatch(WebElement::isDisplayed);
                if (!noVisibleLoader) return false;
                if (d instanceof JavascriptExecutor) {
                    Object active = ((JavascriptExecutor) d)
                            .executeScript("return typeof jQuery !== 'undefined' ? jQuery.active : 0");
                    if (active instanceof Number) {
                        return ((Number) active).intValue() == 0;
                    }
                }
                return true;
            });
        } catch (Exception e) {
            // timeout waiting for AJAX, continue
        }
    }
}
