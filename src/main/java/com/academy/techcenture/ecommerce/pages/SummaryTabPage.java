package com.academy.techcenture.ecommerce.pages;

import com.academy.techcenture.ecommerce.utils.CommonUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class SummaryTabPage extends HomePage {
    public SummaryTabPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//h1[contains(.,'Shopping-cart summary')][1]")
    private WebElement cartHeader;

    @FindBy(xpath = "//ul[@id='order_step']/li")
    private List<WebElement> shoppingToDo;

    @FindBy(xpath = "//li[contains(@class,'first')]//span[1]")
    private WebElement summaryTab;

    @FindBy(xpath = "//li[contains(@class,'four')]//span[1]")
    private WebElement shippingTab;

    @FindBy(xpath = "//table[@id='cart_summary']//tr//th")
    private List<WebElement> summaryOptions;

    @FindBy(css = "td[class='cart_description'] p[class='product-name'] a")
    private WebElement descriptionProductName;

    @FindBy(css = "td[class='cart_description'] small a")
    private WebElement sizeColor;

    @FindBy(xpath = "//span[@class='label label-success']")
    private WebElement availableInStock;

    @FindBy(xpath = "//span[contains(@class,'price special-price')]")
    private WebElement unitPrice;

    @FindBy(xpath = "//span[@class='price-percent-reduction small']")
    private WebElement discount;

    @FindBy(xpath = "//span[@class='old-price']")
    private WebElement oldPrice;

    @FindBy(xpath = "//td[@class='cart_quantity text-center']")
    private WebElement quantityInput;

    @FindBy(xpath = "//td[@class='cart_total']")
    private WebElement totalInput;

    @FindBy(xpath = "//i[@class='icon-trash']")
    private WebElement deleteIcon;

    @FindBy(xpath = "//td[@id='total_product']")
    protected WebElement totalProduct;

    @FindBy(xpath = "//td[@id='total_shipping']")
    protected WebElement totalShipping;

    @FindBy(xpath = "//td[@id='total_price_without_tax']")
    protected WebElement totalWithShipping;

    @FindBy(xpath = "//span[@id='total_price']")
    protected WebElement totalTotal;

    @FindBy(xpath = "(//span[contains(text(),'Proceed to checkout')])[2]")
    protected WebElement checkOutBtn;

    private String[] toDoOptions = {"summary", "sign in", "address", "shipping", "payment"};

    private String[] summaryOptionsExpected = {"product", "description", "avail.", "unit price", "qty", "total", null};

    //address tab
    @FindBy(xpath = "//li[@class='step_current third']//span[1]")
    protected WebElement addressTab;

    @FindBy(id = "id_address_delivery")
    protected WebElement addressField;
    @FindBy(id = "addressesAreEquals")
    protected WebElement addressCheckBox;

    @FindBy(name = "message")
    protected WebElement commentBox;

    @FindBy(xpath = "//div[@class='delivery_option_price']")
    private WebElement deliveryOptPrice;

    @FindBy(xpath = "//td[@class='delivery_option_radio']//input")
    private WebElement shippingRadioBtn;

    @FindBy(xpath = "//div[@class='fancybox-outer']")
    private WebElement warningTermsPopUp;

    @FindBy(xpath = "//a[@title='Close']")
    private WebElement warningTermsPopUpClose;

    @FindBy(linkText = "(Read the Terms of Service)")
    protected WebElement readTermLink;

    @FindBy(xpath = "//div[contains(@class,'content_only')]/p")
    protected List<WebElement> threeRules;

    @FindBy(xpath = "//a[@title='Close']")
    private WebElement closeMustAgreeBtns;

    @FindBy(id = "cgv")
    protected WebElement checkServiceBoxInput;

    @FindBy(tagName = "iframe")
    private WebElement childFrame;

    @FindBy(xpath = "//li[@id='step_end']/span")
    private WebElement paymentTab;

    @FindBy(xpath = "(//td)//img")
    private WebElement dressPaymentPic;

    private void proceedToCheckOut() {

        assertTrue(checkOutBtn.isEnabled(), "Checkout buttons is not Enabled!");
        checkOutBtn.click();

    }

    public void shoppingCartSummary(Map<String, String> data) throws InterruptedException {
        verifyingProductSummaryTabs(data);
        proceedToCheckOut();
        verifyingAddressTab(data);
        proceedToCheckOut();
        verifyShippingTab(data);
        proceedToCheckOut();
        verifyPaymentTab(data);
    }

    public void verifyingProductSummaryTabs(Map<String, String> data) {
        assertEquals(cartHeader.getText().trim().toUpperCase(), "SHOPPING-CART SUMMARY" + "\n" + "YOUR SHOPPING CART CONTAINS: " + data.get("Quantity") + " PRODUCTS", "Header is not correct");
        for (int i = 0; i < shoppingToDo.size(); i++) {
            assertEquals(shoppingToDo.get(i).getText().toLowerCase().substring(4), toDoOptions[i], toDoOptions[i] + "isnt match");
        }

        verifyActiveTab(summaryTab);
        /*String rgbFormat = summaryTab.getCssValue("color");
        String hexcolor = Color.fromString(rgbFormat).asHex(); //converted Into HexFormat
        assertEquals(hexcolor, "#ffffff", "Summary button is selected");*/
        for (int i = 0; i < summaryOptions.size() - 1; i++) {
            assertEquals(summaryOptions.get(i).getText().toLowerCase(), summaryOptionsExpected[i], summaryOptionsExpected[i] + "isnt match");
        }
        assertEquals(descriptionProductName.getText().trim(), data.get("Name"), "Product name isn't correct");
        String[] colorAndSize = sizeColor.getText().split(",");
        String[] uiColor = colorAndSize[0].split(":");
        String[] uiSize = colorAndSize[1].split(":");
        assertEquals(uiColor[1].trim(), data.get("PickColor"), "Color isn't correct");
        assertEquals(uiSize[1].trim(), data.get("Size"), "Size isn't correct");
        assertTrue(availableInStock.isDisplayed(), "In Stoke isn't displayed");
        assertEquals(unitPrice.getText().replace("$", ""), data.get("PriceAfterDiscount"), "Price isn't correct");
        assertEquals(discount.getText().replaceAll("[-%]", "").trim(), data.get("Discount"), "Discount isn't correct");
        assertEquals(oldPrice.getText().replace("$", ""), data.get("PriceBeforeDiscount"), "Price before discount isn't correct");
        assertTrue(quantityInput.isDisplayed(), "Quantity isn't displayed");
        double total = Double.parseDouble(data.get("Quantity")) * Double.parseDouble(data.get("PriceAfterDiscount"));
        assertEquals(totalInput.getText().replace("$", ""), Double.toString(total), "Total Input isn't correct");
        assertTrue(deleteIcon.isEnabled(), "Delete Icon isn't enabled");
        assertEquals(totalProduct.getText().replace("$", ""), Double.toString(total), "Total Input isn't correct");
        assertEquals(totalShipping.getText().replace("$", "").substring(0, 3), data.get("ShippingCost"), "Shipping Cost isn't correct");
        double totalPlusShipping = Double.parseDouble(data.get("ShippingCost")) + total;
        assertEquals(totalWithShipping.getText().replace("$", ""), Double.toString(totalPlusShipping), "Total plus Shipping isn't correct");
        assertEquals(totalTotal.getText().replace("$", ""), Double.toString(totalPlusShipping), "Finished Total isn't correct");
        assertTrue(checkOutBtn.isEnabled(), "Check Out Button isn't enabled");
    }

    public void verifyingAddressTab(Map<String, String> data) {

        verifyActiveTab(addressTab);

        assertTrue(addressField.isEnabled(), "Address Field is enabled");
        if (!addressCheckBox.isSelected()) {
            addressCheckBox.click();
        }
        assertTrue(commentBox.isEnabled(), "Address Field is enabled");
        commentBox.sendKeys("There should be a random comment, but may be later");
    }

    public void verifyShippingTab(Map<String, String> data) throws InterruptedException {

        verifyActiveTab(shippingTab);

        assertEquals(Double.parseDouble(deliveryOptPrice.getText().trim().replace("$", "")), Double.parseDouble(data.get("ShippingCost")), "Shipping price doesn't match");
        assertTrue(shippingRadioBtn.isSelected(), "Radio button is not selected!");
        proceedToCheckOut();
        assertTrue(warningTermsPopUp.isDisplayed(), "Warning for terms is not Displayed!");
        warningTermsPopUpClose.click();

        assertTrue(readTermLink.isDisplayed(), "read term link is not displayed");
        readTermLink.click();
        Thread.sleep(2000);

        //Switching to child html (iFrame)
        switchToFrame(childFrame);

        assertEquals(threeRules.get(0).getText().trim(), data.get("Rule1").trim(), "Rule 1 doesnt match with data we have");
        assertEquals(threeRules.get(1).getText().trim(), data.get("Rule2").trim(), "Rule 2 doesnt match with data we have");
        assertEquals(threeRules.get(2).getText().trim(), data.get("Rule3").trim(), "Rule 3 doesnt match with data we have");

        //Switching back to main html
        switchToFrame(0);

        assertTrue(closeMustAgreeBtns.isDisplayed(), "Close button for tearms is not displayed");
        closeMustAgreeBtns.click();

        if (!checkServiceBoxInput.isSelected()) {
            checkServiceBoxInput.click();
        }

    }


    private void switchToFrame(WebElement frame) {
        driver.switchTo().frame(frame);
    }

    private void switchToFrame(int index) {
        if (index == 0) {
            driver.switchTo().defaultContent(); //switiching to parent window
        } else {
            driver.switchTo().frame(index);
        }

    }

    private void verifyActiveTab(WebElement tab) {

        String rgbFormat = tab.getCssValue("color"); // rgb(255,255,255)
        String hexcolor = Color.fromString(rgbFormat).asHex(); //converted Into HexFormat #fffff
        assertEquals(hexcolor, "#ffffff", "Tab button is selected");
    }

    public void verifyPaymentTab(Map<String, String> data) {
        verifyActiveTab(paymentTab);
        assertTrue(dressPaymentPic.isDisplayed(), "Dress pic is NOT displayed");
        assertEquals(descriptionProductName.getText().trim(),data.get("Name").trim() , "Name of the product is NOT displayed");
        String[] colorAndSize = sizeColor.getText().split(",");
        String[] uiColor = colorAndSize[0].split(":");
        String[] uiSize = colorAndSize[1].split(":");
        assertEquals(uiColor[1].trim(), data.get("PickColor"), "Color isn't correct");
        assertEquals(uiSize[1].trim(), data.get("Size"), "Size isn't correct");
        assertTrue(availableInStock.isDisplayed(), "In Stoke isn't displayed");
        assertEquals(unitPrice.getText().replace("$", ""), data.get("PriceAfterDiscount"), "Price isn't correct");
        assertEquals(discount.getText().replaceAll("[-%]", "").trim(), data.get("Discount"), "Discount isn't correct");
        assertEquals(oldPrice.getText().replace("$", ""), data.get("PriceBeforeDiscount"), "Price before discount isn't correct");
    }


}





