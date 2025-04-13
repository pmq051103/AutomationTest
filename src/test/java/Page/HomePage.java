package Page;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
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
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Home\")")
    WebElement btnHome;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đăng nhập\")")
    WebElement btnLogin;
    
    @AndroidFindBy(xpath  = "//android.widget.ImageView[@resource-id=\"com.shopee.vn:id/action_btn1\"]")
    WebElement btnSearchByImage;
    
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
    
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"com.shopee.vn:id/cart_btn\")")
    WebElement btnCart;
    
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Xem tất cả\")")
    WebElement btnViewAll;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đã thích\")")
    WebElement btnLiked;
    
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
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	wait.until(ExpectedConditions.elementToBeClickable(btnMe)).click();
		return this;
    }
    
    
    public HomePage clickbtnLogin() {
    	btnLogin.click();
    	return this;
    }
    
    public HomePage clickBtnHome() throws InterruptedException {
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.elementToBeClickable(btnHome)).click();
    	return this;
    }
    
    public HomePage clickIconSearch() {
    	wait.until(ExpectedConditions.elementToBeClickable(iconSearch)).click();
    	return this;

    }
    
    public HomePage clickIconCart() {
    	Allure.step("Nhấn vào giỏ hàng");
    	wait.until(ExpectedConditions.elementToBeClickable(btnCart)).click();
    	return this;

    }
    
    public HomePage clickBtnSearchByImage() {
    	Allure.step("Nhấn vào nút tìm kiếm bằng hình ảnh");
    	wait.until(ExpectedConditions.elementToBeClickable(btnSearchByImage)).click();
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
    
    public HomePage clickBtnViewAll() {
      	Allure.step("Nhấn vào mục xem tất cả tiện ích");
    	wait.until(ExpectedConditions.elementToBeClickable(btnViewAll)).click();
    	return this;
    }  
    
    public HomePage clickBtnLiked() {
      	Allure.step("Nhấn vào mục đã thích");
    	wait.until(ExpectedConditions.elementToBeClickable(btnLiked)).click();
    	return this;
    }
    
    public HomePage clickFavoriteAS() throws InterruptedException  {
    	Thread.sleep(1000);
    	swipeToExactPosition(527, 1759, 1003, 1500);
    	Thread.sleep(1000);
    	clickBtnViewAll();
    	Thread.sleep(1000);
    	return clickBtnLiked();
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

  
    private void swipeToExactPosition(int startX, int startY, int endY, int duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // Chạm vào tọa độ (startX, startY)
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Vuốt đến tọa độ (startX, endY) với tốc độ được điều chỉnh
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(duration), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    
}
