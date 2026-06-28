package com.shop.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CategoryPage extends BasePage {

    private static final String BASE_URL = "https://demowebshop.tricentis.com";

    private By pageTitle = By.cssSelector(".page-title h1");
    private By productItems = By.cssSelector(".product-item");
    private By productTitles = By.cssSelector(".product-title a");
    private By addToCartButtons = By.cssSelector("input.button-2.product-box-add-to-cart-button");

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public void goToCategory(String categoryName) {
        String slug = categoryName.toLowerCase().replace(" ", "-");
        driver.get(BASE_URL + "/" + slug);
        waitForProductList();
    }

    private void waitForProductList() {
        try {
            wait.until(d -> !d.findElements(productItems).isEmpty());
        } catch (Exception e) {
            System.out.println("  [category] Product list did not load, reloading...");
            driver.get(driver.getCurrentUrl());
            try {
                wait.until(d -> !d.findElements(productItems).isEmpty());
            } catch (Exception e2) {
                System.out.println("  [category] Product list still not loaded after reload");
            }
        }
    }

    public void goToSubcategory(String categoryName, String subcategoryName) {
        String slug = subcategoryName.toLowerCase().replace(" ", "-");
        driver.get(BASE_URL + "/" + slug);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getProductCount() {
        return driver.findElements(productItems).size();
    }

    public List<WebElement> getProductTitles() {
        return driver.findElements(productTitles);
    }

    public void addFirstProductToCart() {
        List<WebElement> buttons = driver.findElements(addToCartButtons);
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
    }

    public void addAllProductsToCart() {
        List<WebElement> buttons = driver.findElements(addToCartButtons);
        for (WebElement button : buttons) {
            button.click();
            waitForAjax();
        }
    }

    public List<String> getProductLinks() {
        return driver.findElements(productTitles).stream()
                .map(e -> e.getAttribute("href"))
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean isProductListDisplayed() {
        return getProductCount() > 0;
    }

    public void clickProduct(String productName) {
        try {
            click(By.linkText(productName));
        } catch (Exception e) {
            try {
                click(By.partialLinkText(productName));
            } catch (Exception e2) {
                System.out.println("  [category] Product '" + productName + "' not found, reloading page...");
                driver.get(driver.getCurrentUrl());
                try {
                    click(By.linkText(productName));
                } catch (Exception e3) {
                    click(By.partialLinkText(productName));
                }
            }
        }
    }
}
