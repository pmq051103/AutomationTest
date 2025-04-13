package utils.listeners;

import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class Utils {
	AndroidDriver driver;
	 private WebDriverWait wait;
	
    public Utils(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
	public static void checkAlertMessage(SoftAssert softAssert, AndroidDriver driver, String expectedMessage) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
	    try {
	        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
	        String actualMessage = alert.getText();

	        if (!expectedMessage.equals(actualMessage)) {
	            Allure.step("Alert không đúng! Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
	            AllureTestListener.saveScreenshotPNG(driver);
	        }

	        softAssert.assertEquals(actualMessage, expectedMessage, "Alert không khớp!");
	        alert.dismiss(); // Đóng Alert
	    } catch (Exception e) {
	        Allure.step("Không tìm thấy Alert!");
	        AllureTestListener.saveScreenshotPNG(driver);
	    }
	}
	
	private static void verifyAndLog(AndroidDriver driver, String fieldName, String actualValue, String expectedValue, SoftAssert softAssert) {
	    try {
	        Allure.step("Kiểm tra " + fieldName);
	        
	        if (!expectedValue.equals(actualValue)) {
	            // Khi giá trị không khớp
	            AllureTestListener.saveScreenshotPNG(driver);
	            Allure.addAttachment("Thông báo lỗi " + fieldName, 
	                "Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
	        } else {
	            // Khi giá trị khớp
	            Allure.step("Thông báo " + fieldName + " khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
	        }
	        
	        // Thêm vào kiểm tra với SoftAssert
	        softAssert.assertTrue(expectedValue.equals(actualValue),
	                "Thông báo lỗi " + fieldName + " không khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
	    } catch (Exception e) {
	        Allure.addAttachment("Lỗi trong verifyAndLog", e.getMessage());
	    }
	}
	
    public boolean confirmToast(String toastNotificationActual) {
        return Allure.step("Kiểm tra thông báo Toast", () -> {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

                WebElement toastElement = driver.findElement(By.xpath("//android.widget.Toast[1]"));
                String toast = toastElement.getText();
                System.out.println("Toast: " + toast);

                // Kiểm tra thông báo Toast với giá trị mong đợi
                boolean result = toast.equals(toastNotificationActual);
                if (result) {
                    Allure.step("Thành công. Thông báo mong đợi: " + toast + " | Thực tế: " + toastNotificationActual);
                } else {
                	throw new AssertionError("Thông báo lỗi không khớp.Mong đợi: " + toastNotificationActual + " | Thực tế: " + toast);
                }
                return result;
            } catch (Exception e) {
                
                Allure.addAttachment("Lỗi khi lấy Toast", e.getMessage());
                return false;
            }
        });
    }

	public void scrollDown() {
        int startX = driver.manage().window().getSize().width / 2;
        int startY = (int) (driver.manage().window().getSize().height * 0.7);
        int endY = (int) (driver.manage().window().getSize().height * 0.3);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }



}
