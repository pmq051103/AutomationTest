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

public class Basic {
    public static AndroidDriver driver;
    public static AppiumDriverLocalService service;

//    @BeforeClass
//    public void setupSuite() throws MalformedURLException {
//        configureAppium();
//    }

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
    }

    @AfterClass
    public void afterClass() throws InterruptedException {
        Thread.sleep(2000);
        if (driver != null) {
            driver.quit();
        }
    }
}
