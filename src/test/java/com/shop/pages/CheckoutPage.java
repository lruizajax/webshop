package com.shop.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

    private static final String URL = "https://demowebshop.tricentis.com/onepagecheckout";

    private By billingContinue = By.cssSelector("#billing-buttons-container input.button-1");
    private By shippingContinue = By.cssSelector("#shipping-buttons-container input.button-1");
    private By shippingMethodContinue = By.cssSelector("#shipping-method-buttons-container input.button-1");
    private By paymentMethodContinue = By.cssSelector("#payment-method-buttons-container input.button-1");
    private By paymentInfoContinue = By.cssSelector("#payment-info-buttons-container input.button-1");
    private By confirmButton = By.cssSelector("input.button-1.confirm-order-next-step-button");
    private By orderNumberItem = By.xpath("//li[contains(text(),'Order number')]");
    private By orderCompletedMessage = By.cssSelector(".order-completed .title strong");
    private By checkoutCompleted = By.cssSelector(".checkout-completed");

    private By billingFirstName = By.id("BillingNewAddress_FirstName");
    private By billingLastName = By.id("BillingNewAddress_LastName");
    private By billingEmail = By.id("BillingNewAddress_Email");
    private By billingCountry = By.id("BillingNewAddress_CountryId");
    private By billingCity = By.id("BillingNewAddress_City");
    private By billingAddress1 = By.id("BillingNewAddress_Address1");
    private By billingZip = By.id("BillingNewAddress_ZipPostalCode");
    private By billingPhone = By.id("BillingNewAddress_PhoneNumber");

    private By groundShipping = By.id("shippingoption_0");
    private By moneyOrderPayment = By.id("paymentmethod_1");

    private org.openqa.selenium.support.ui.WebDriverWait checkoutWait;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        this.checkoutWait = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void fillBillingAddress(String firstName, String lastName, String email,
                                   String country, String city, String address,
                                   String zip, String phone) {
        By addressSelect = By.id("billing-address-select");
        try {
            if (isDisplayed(addressSelect)) {
                new Select(driver.findElement(addressSelect)).selectByVisibleText("New Address");
                wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
            }
        } catch (Exception e) {
            System.out.println("  [checkout] No address dropdown, proceeding...");
        }
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
            type(billingFirstName, firstName);
            type(billingLastName, lastName);
            type(billingEmail, email);
            if (isDisplayed(billingCountry)) {
                new Select(driver.findElement(billingCountry)).selectByVisibleText(country);
            }
            type(billingCity, city);
            type(billingAddress1, address);
            type(billingZip, zip);
            type(billingPhone, phone);
            click(billingContinue);
            waitForCheckoutLoading();
        } catch (Exception e) {
            System.out.println("  [checkout] Billing form not found, trying Continue");
            try {
                click(billingContinue);
            } catch (Exception ex) {
                System.out.println("  [checkout] Could not proceed with billing: " + ex.getMessage());
            }
        }
    }

    public void continueShipping() {
        try {
            checkoutWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(shippingContinue),
                    ExpectedConditions.visibilityOfElementLocated(shippingMethodContinue)
            ));
        } catch (Exception e) {
            System.out.println("  [checkout] No shipping section appeared, continuing...");
            return;
        }
        if (isDisplayed(shippingContinue)) {
            click(shippingContinue);
            waitForCheckoutLoading();
        }
    }

    public void selectShippingMethodGround() {
        try {
            checkoutWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(groundShipping),
                    ExpectedConditions.visibilityOfElementLocated(paymentMethodContinue)
            ));
        } catch (Exception e) {
            System.out.println("  [checkout] Shipping method section not found");
        }
        if (isDisplayed(groundShipping)) {
            click(groundShipping);
            click(shippingMethodContinue);
            waitForCheckoutLoading();
        }
    }

    public void selectPaymentMethodMoneyOrder() {
        try {
            checkoutWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(moneyOrderPayment),
                    ExpectedConditions.visibilityOfElementLocated(confirmButton)
            ));
        } catch (Exception e) {
            System.out.println("  [checkout] Payment method section not found");
        }
        if (isDisplayed(moneyOrderPayment)) {
            click(moneyOrderPayment);
            click(paymentMethodContinue);
            try {
                checkoutWait.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(paymentInfoContinue),
                        ExpectedConditions.visibilityOfElementLocated(confirmButton)
                ));
            } catch (Exception ex) {
                System.out.println("  [checkout] Neither payment info nor confirm appeared");
            }
            if (isDisplayed(paymentInfoContinue)) {
                click(paymentInfoContinue);
            }
        }
    }

    public void confirmOrder() {
        try {
            checkoutWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(orderCompletedMessage),
                    ExpectedConditions.visibilityOfElementLocated(confirmButton)
            ));
        } catch (Exception e) {
            System.out.println("  [checkout] Confirm button not visible within timeout: " + e.getMessage());
        }
        if (isDisplayed(orderCompletedMessage)) {
            return;
        }
        if (isDisplayed(confirmButton)) {
            try {
                waitForClickable(confirmButton, Duration.ofSeconds(30));
                click(confirmButton);
            } catch (Exception e) {
                System.out.println("  [checkout] Confirm button not clickable: " + e.getMessage());
            }
        } else {
            System.out.println("  [checkout] Confirm button not displayed");
        }
        waitForOrderConfirmation();
    }

    public boolean isOrderSuccess() {
        try {
            checkoutWait.until(ExpectedConditions.visibilityOfElementLocated(orderCompletedMessage));
            return true;
        } catch (Exception e) {
            System.out.println("  [checkout] Current URL: " + driver.getCurrentUrl());
            System.out.println("  [checkout] Page title: " + driver.getTitle());
            return false;
        }
    }

    public String getOrderNumber() {
        try {
            String text = getText(orderNumberItem);
            return text.replaceAll("\\D+", "");
        } catch (Exception e) {
            String pageSource = driver.getPageSource();
            int idx = pageSource.indexOf("Order number");
            if (idx >= 0) {
                String snippet = pageSource.substring(idx, Math.min(idx + 100, pageSource.length()));
                System.out.println("  [checkout] Order number snippet: " + snippet.replace('\n', ' '));
            }
            return "0";
        }
    }

    private void waitForCheckoutLoading() {
        try {
            checkoutWait.until(d -> {
                java.util.List<org.openqa.selenium.WebElement> loaders = d.findElements(
                        By.cssSelector(".ajax-loading-block-window"));
                return loaders.isEmpty() || loaders.stream().noneMatch(org.openqa.selenium.WebElement::isDisplayed);
            });
        } catch (Exception e) {
            System.out.println("  [checkout] Loading overlay did not disappear");
        }
    }

    public void waitForCheckoutPage() {
        try {
            checkoutWait.until(ExpectedConditions.urlContains("onepagecheckout"));
        } catch (Exception e) {
            System.out.println("  [checkout] Not on checkout page, current URL: " + driver.getCurrentUrl());
            System.out.println("  [checkout] Navigating to checkout URL directly...");
            driver.get(URL);
            try {
                checkoutWait.until(ExpectedConditions.urlContains("onepagecheckout"));
            } catch (Exception ex) {
                System.out.println("  [checkout] Direct navigation also failed to reach checkout page");
            }
        }
    }

    private void waitForOrderConfirmation() {
        try {
            checkoutWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(orderCompletedMessage),
                    ExpectedConditions.urlContains("completed")
            ));
        } catch (Exception e) {
            System.out.println("  [checkout] Timed out waiting for order confirmation");
        }
    }
}
