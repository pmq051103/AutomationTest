package utils.extentreport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import Base.Basic;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager extends Basic{
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    public static ExtentReports extent = ExtentManager.getExtentReports();

    public static ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentTest saveToReport(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }
    
    



    public static void addScreenShot(String message) {
        if (driver instanceof TakesScreenshot) {
            String base64Image = "data:image/png;base64," + ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            getTest().log(Status.INFO, message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
        } else {
            getTest().log(Status.WARNING, "Không thể chụp ảnh màn hình: WebDriver không hỗ trợ.");
        }
    }

    public static void addScreenShot(Status status, String message) {
        if (driver instanceof TakesScreenshot) {
            String base64Image = "data:image/png;base64," + ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            getTest().log(status, message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
        } else {
            getTest().log(Status.WARNING, "Không thể chụp ảnh màn hình: WebDriver không hỗ trợ.");
        }
    }

    public static void logMessage(String message) {
        getTest().log(Status.INFO, message);
    }

    public static void logMessage(Status status, String message) {
        getTest().log(status, message);
    }
}
