package Page;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class HomePage {
	private AndroidDriver driver;
	private WebDriverWait wait;
    
    @AndroidFindBy(id = "com.shopee.vn:id/btn")
    WebElement btnStart;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Tôi\")")
    WebElement btnMe;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đăng nhập\")")
    WebElement btnLogin;
    
    @AndroidFindBy(id= "com.shopee.vn:id/home_btn")
    WebElement btnBack;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"labelUserName\")")
    WebElement lblUserName;
    
    @AndroidFindBy(id = "com.shopee.vn:id/search_icon")
    WebElement iconSearch;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(1)")
    WebElement btnGoToShop;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Sản phẩm của tôi\")")
    WebElement btnMyProduct;
    
    @AndroidFindBy(uiAutomator = "add_new_product_button")
    WebElement btnAddProduct;
    
    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    
    
    
    public HomePage clickBtnStart() {
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	wait.until(ExpectedConditions.elementToBeClickable(btnStart)).click();
    	return this;
    }
    

    public HomePage clickBtnMe() {
    	wait.until(ExpectedConditions.elementToBeClickable(btnMe)).click();
		return this;
    }
    
    
    public HomePage clickbtnLogin() {
    	wait.until(ExpectedConditions.elementToBeClickable(btnLogin)).click();
    	return this;
    }
    
    public HomePage clickIconSearch() {
    	wait.until(ExpectedConditions.elementToBeClickable(iconSearch)).click();
    	return this;

    }
    
    public HomePage clickBtnBack() {
    	wait.until(ExpectedConditions.elementToBeClickable(btnBack)).click();
    	return this;
    }
    
    public HomePage clickBtnGoToShop() {
    	wait.until(ExpectedConditions.elementToBeClickable(btnGoToShop)).click();
    	return this;
    }
    
    public HomePage clickBtnMyProduct() {
    	wait.until(ExpectedConditions.elementToBeClickable(btnMyProduct)).click();
    	return this;
    }
    
    public HomePage clickBtnAddNewProduct() {
    	wait.until(ExpectedConditions.elementToBeClickable(btnAddProduct)).click();
    	return this;
    }
    
    
    
    public HomePage clickLoginAS() {
    	clickBtnMe();
    	return clickbtnLogin();
    }
    
    public HomePage clickAddNewProductAS() {
    	clickBtnMe();
    	clickBtnGoToShop();
    	clickBtnMyProduct();
    	return clickBtnAddNewProduct();
    }
    
    @Step("Kiểm tra kết quả đăng nhập")
    public boolean confirmLogin(String usernameActual) {
        try {
            wait.until(ExpectedConditions.visibilityOf(lblUserName));
            String username = lblUserName.getText();

            // Chụp ảnh màn hình
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Allure.addAttachment("Ảnh màn hình đăng nhập", new FileInputStream(screenshot));

            if (username.equals(usernameActual)) {
                Allure.step("✅ Thành công. UserName mong đợi: " + username + " | Thực tế: " + usernameActual);
                return true;
            } else {
                Allure.addAttachment("❌ Thông báo lỗi", "Mong đợi: " + username + " | Thực tế: " + usernameActual);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

  
    

    
}
