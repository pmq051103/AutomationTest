package utils.listeners;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;

public class Utils {
	public static void checkAlertMessage(SoftAssert softAssert, AndroidDriver driver, String expectedMessage) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
	    try {
	        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
	        String actualMessage = alert.getText();

	        if (!expectedMessage.equals(actualMessage)) {
	            Allure.step("❌ Alert không đúng! Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
	            AllureTestListener.saveScreenshotPNG(driver); // Chụp màn hình
	        }

	        softAssert.assertEquals(actualMessage, expectedMessage, "Alert không khớp!");
	        alert.dismiss(); // Đóng Alert
	    } catch (Exception e) {
	        Allure.step("❌ Không tìm thấy Alert!");
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




}
