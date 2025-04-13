package Page;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Allure;

public class ProductDetailPage {
	private AndroidDriver driver;
	private WebDriverWait wait;
	private Actions ac;
	
	@AndroidFindBy(xpath  = "//android.widget.TextView[@resource-id=\"labelProductPageProductName\"]")
	WebElement productName;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"buttonProductAddCart\"]")
	WebElement btnAddToCart;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"buttonOption_unselected\"]")
	List<WebElement> option;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"buttonOption_unselected\"]//android.widget.TextView")
	List<WebElement> options;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"buttonOption_selected\"]//android.widget.TextView")
	WebElement optionSelected;
	
	@AndroidFindBy(className = "android.widget.EditText")
	WebElement inputQuantity;
	
	@AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup[6]")
	WebElement btnAdd;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"buttonActionBarView\"]/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup")
	WebElement btnCart;
	
    @AndroidFindBy(className = "android.widget.Toast")
    WebElement toastNotification;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"labelItemStock\")")
    WebElement itemStock;
    
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"sectionTierVariation\"]/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup/android.widget.TextView")
    WebElement message;
    
	@AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"buttonItemLike\")")
	WebElement btnLike ;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"buttonItemLiked\")")
	WebElement btnUnLike ;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Xem\")")
	WebElement btnView;
	
	@AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup[5]/android.view.ViewGroup[1]/android.widget.TextView[contains(@text, '!')]")
	WebElement toast;
	
	
	public ProductDetailPage clickBtnAddToCard() {
		Allure.step("Nhấn icon giỏ hàng");
    	wait.until(ExpectedConditions.elementToBeClickable(btnAddToCart)).click();
    	return this;
	}
	
	public ProductDetailPage chooseOption() {
	    return Allure.step("Chọn một tùy chọn bất kỳ", () -> {
	        if (option.isEmpty()) {
	            Allure.step("Không có tùy chọn nào.");
	            return this;
	        }

	        Random random = new Random();
	        WebElement randomOption = option.get(random.nextInt(option.size())); 

	        wait.until(ExpectedConditions.elementToBeClickable(randomOption)).click();
	        String optionText = optionSelected.getText().trim();

	        Allure.step("Đã chọn tùy chọn: " + optionText);
	        return this;
	    });
	}
	
	
	private ProductDetailPage selectOptionByText(String expectedOption) {
	    return Allure.step("Chọn tùy chọn: " + expectedOption, () -> {
	        if (options.isEmpty()) {  // Kiểm tra danh sách option (sửa lỗi `option.isEmpty()`)
	            Allure.step("Không có tùy chọn nào khả dụng.");
	            return this;
	        }

	        for (WebElement op : options) {
	            String optionText = op.getText().trim();
	            System.out.println(optionText);
	            if (optionText.equalsIgnoreCase(expectedOption)) {
	            	op.click();
	                Allure.step("Đã chọn option: " + expectedOption);
	                return this;
	            }
	        }


	        Allure.step("Không tìm thấy option mong đợi: " + expectedOption);
	        return this;
	    });
	}

	
	public ProductDetailPage clickBtnAdd() {
		Allure.step("Nhấn nút thêm vào giỏ hàng");
    	wait.until(ExpectedConditions.elementToBeClickable(btnAdd)).click();
    	return this;
	}
	
	public ProductDetailPage clickBtnCart() throws InterruptedException {
		Thread.sleep(1500);
		Allure.step("Nhấn nút giỏ hàng");
    	wait.until(ExpectedConditions.elementToBeClickable(btnCart)).click();
    	return this;
	}
	
	public ProductDetailPage clickBtnView() throws InterruptedException {
		Allure.step("Nhấn nút xem danh sách yêu thích");
    	wait.until(ExpectedConditions.elementToBeClickable(btnView)).click();
    	return this;
	}
	
	public ProductDetailPage clickBtnLike() throws InterruptedException {
		Thread.sleep(1500);
		Allure.step("Nhấn icon yêu thích");
    	wait.until(ExpectedConditions.elementToBeClickable(btnLike)).click();
    	return this;
	}
	
	public ProductDetailPage sendkeyInputQuantity(int quantity) {
		Allure.step("Nhập số lượng : "+ quantity);
		wait.until(ExpectedConditions.visibilityOf(inputQuantity));
		inputQuantity.clear();
		inputQuantity.sendKeys(String.valueOf(quantity));
		return this;
	}
	
	public String getProductName() {
	    wait.until(ExpectedConditions.visibilityOf(productName));
	    String name = productName.getText().trim();

	    if (name.startsWith("0")) {
	        name = name.replaceFirst("^0+", "");
	    }
	    return name;
	}
	
	public String getOptionSelected() {
		wait.until(ExpectedConditions.visibilityOf(optionSelected));
		return optionSelected.getText();
	}
	
	public ProductDetailPage addToCardAS() throws InterruptedException {
		Thread.sleep(1000);
		clickBtnAddToCard();
		Thread.sleep(1000);
		chooseOption();
		return this;
	}
	
	public ProductDetailPage addToCardWithOptionText(String option) throws InterruptedException {
		Thread.sleep(1000);
		clickBtnAddToCard();
		Thread.sleep(1000);
		selectOptionByText(option);
		return this;
	}
	
    public ProductDetailPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ac = new Actions(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    
    public boolean checkToastMessage(String expectedMessage) {
        return Allure.step("Kiểm tra thông báo hiển thị", () -> {
            try {
            	
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast")));
                WebElement toast = driver.findElement(By.xpath("//android.widget.Toast"));
                String actualMessage = toast.getText();

                if (actualMessage.equals(expectedMessage)) {
                    Allure.step("Toast hiển thị đúng: " + actualMessage);
                    System.out.println("Nội dung toast " + actualMessage);
                    return true;
                } else {
                    Allure.step("Toast hiển thị sai! Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
                    return false;
                }
            } catch (Exception e) {
                Allure.step("Không tìm thấy thông báo lỗi.");
                return false;
            }
        });
    }
    
    
    
    
    public int getStockQuantity() {
        String stockText = itemStock.getText(); 
        String stockNumber = stockText.replace("Kho: ", "").trim();
        return Integer.parseInt(stockNumber);
    }
    
    public boolean verifyMessage(String expectedMessage, SoftAssert softAssert) {
        return Allure.step("Kiểm tra thông báo hiển thị trên màn hình", () -> {
            String actualMessage = message.getText().trim();
            boolean isMatch = expectedMessage.equals(actualMessage);
            if (!isMatch) {
                Allure.addAttachment("Thông báo không đúng", 
                    "Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
            } else {
                Allure.step("Thông báo chính xác: " + actualMessage);
            }

            softAssert.assertEquals(actualMessage, expectedMessage, 
                "Thông báo không khớp. Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);

            return isMatch;
        });
    }
    
    public boolean verifyToast(String expectedMessage, SoftAssert softAssert) {
        return Allure.step("Kiểm tra thông báo hiển thị trên màn hình", () -> {
            String actualMessage = toast.getText().trim();
            boolean isMatch = expectedMessage.equals(actualMessage);
            if (!isMatch) {
                Allure.addAttachment("Thông báo không đúng", 
                    "Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
            } else {
                Allure.step("Thông báo chính xác: " + actualMessage);
            }

            softAssert.assertEquals(actualMessage, expectedMessage, 
                "Thông báo không khớp. Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);

            return isMatch;
        });
    }

}
