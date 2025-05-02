package Base;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.appium.Eyes;

public class Basic {
    public static AndroidDriver driver;
    public static AppiumDriverLocalService service;
    public static Eyes eyes;

    public void configureAppium() throws MalformedURLException {
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Users\\ASUS\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .withIPAddress("127.0.0.1").usingPort(4723).build();
        service.start();

        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("platformName", "Android");
        options.setCapability("deviceName", "OPPO Reno4");
        options.setCapability("appium:automationName", "UiAutomator2");
        options.setCapability("appPackage", "com.shopee.vn");
        options.setCapability("appActivity", "com.shopee.app.ui.home.HomeActivity_");
        options.setCapability("ignoreHiddenApiPolicyError", true);
        options.setCapability("autoGrantPermissions", true);
        options.setCapability("noReset", false);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        
        eyes = new Eyes();
        eyes.setApiKey("3iVKq8I92vWQLNmwdnPgJA7joqjdQx97BJnREkQKiAv4110"); 
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setBatch(new BatchInfo("Shopee Android Test Batch"));
    }

    @AfterClass
    public void afterClass() throws InterruptedException {
        Thread.sleep(2000);
        if (eyes != null) {
            eyes.abortIfNotClosed();
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
