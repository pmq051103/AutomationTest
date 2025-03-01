package utils.listeners;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureTestListener implements ITestListener {

    // Ghi log text vào báo cáo Allure
    @Attachment(value = "Lỗi: {0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    // Chụp màn hình khi lỗi xảy ra
    @Attachment(value = "Ảnh lỗi", type = "image/png")
    public static byte[] saveScreenshotPNG(AndroidDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    @Attachment(value = "Ảnh chụp màn hình", type = "image/png")
    public static byte[] captureScreenshot(AndroidDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        AndroidDriver driver = (AndroidDriver) iTestResult.getTestContext().getAttribute("driver");
        if (driver != null) {
            byte[] screenshot = saveScreenshotPNG(driver); // Chụp ảnh lỗi
            Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", screenshot); 
        }
        saveTextLog("Test thất bại: " + iTestResult.getName());
    }
    
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
    	AndroidDriver driver = (AndroidDriver) iTestResult.getTestContext().getAttribute("driver");
        if (driver != null) {
            byte[] screenshot = captureScreenshot(driver);
            Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", screenshot); 
        }
    }

}
