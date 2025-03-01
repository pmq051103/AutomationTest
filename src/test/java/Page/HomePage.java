package Page;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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
    

    
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"labelUserName\")")
    WebElement lblUserName;
    
    @AndroidFindBy(id = "com.shopee.vn:id/search_icon")
    WebElement iconSearch;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Shop của tôi\")")
    WebElement btnGoToShop;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Sản phẩm của tôi\")")
    WebElement btnMyProduct;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đã hiểu\")")
    WebElement btnUnderstood;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Bỏ qua tất cả\")")
    WebElement skipInstructions;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đồng ý\")")
    WebElement btnAgree;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Thêm 1 sản phẩm mới\")")
    WebElement btnAddProduct;
    
    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	btnMe.click();
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
    	((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        return this;
    }
    
    public HomePage clickBtnGoToShop() {
    	Allure.step("Nhấn vào mục Shop của tôi");
    	wait.until(ExpectedConditions.elementToBeClickable(btnGoToShop)).click();
    	return this;
    }
    
    public HomePage clickBtnMyProduct() {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Allure.step("Nhấn vào mục sản phẩm của tôi");
    	wait.until(ExpectedConditions.elementToBeClickable(btnMyProduct)).click();
    	return this;
    }
    
    public HomePage clickSkipInstructions() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3)); // Chờ tối đa 2s

        try {
            if (shortWait.until(ExpectedConditions.visibilityOf(btnUnderstood)).isDisplayed()) {
                shortWait.until(ExpectedConditions.elementToBeClickable(btnUnderstood)).click();
             
            }
            
        } catch (TimeoutException e) {
            System.out.println("btnUnderstood không xuất hiện, bỏ qua.");
        }

//        try {
//            if (shortWait.until(ExpectedConditions.visibilityOf(skipInstructions)).isDisplayed()) {
//                shortWait.until(ExpectedConditions.elementToBeClickable(skipInstructions)).click();
//            }
//        } catch (TimeoutException e) {
//            System.out.println("skipInstructions không xuất hiện, bỏ qua.");
//        }
//
//        try {
//            if (shortWait.until(ExpectedConditions.visibilityOf(btnAgree)).isDisplayed()) {
//                shortWait.until(ExpectedConditions.elementToBeClickable(btnAgree)).click();
//            }
//        } catch (TimeoutException e) {
//            System.out.println("btnAgree không xuất hiện, bỏ qua.");
//        }

        return this;
    }

    
    public HomePage clickBtnAddNewProduct() {
    	Allure.step("Nhấn nút thêm 1 sản phẩm mới");
    	wait.until(ExpectedConditions.elementToBeClickable(btnAddProduct)).click();
    	return this;
    }
    
    
    
    public HomePage clickLoginAS() {
    	clickBtnMe();
    	return clickbtnLogin();
    }
    
    public HomePage clickAddNewProductAS() {
    	//clickBtnMe();
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	clickBtnGoToShop();
    	clickSkipInstructions();
    	clickBtnMyProduct();
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	clickBtnAddNewProduct();
    	return this;
    }
    
    @Step("Kiểm tra kết quả đăng nhập")
    public boolean confirmLogin(String usernameActual) {
        try {
            wait.until(ExpectedConditions.visibilityOf(lblUserName));
            String username = lblUserName.getText();

            if (username.equals(usernameActual)) {
                Allure.step("Thành công. UserName mong đợi: " + username + " | Thực tế: " + usernameActual);
                return true;
            } else {
                Allure.step("Thông báo lỗi Mong đợi: " + username + " | Thực tế: " + usernameActual);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

  
    

    
}
