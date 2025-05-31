package utils.listeners;

import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.Region;
import com.applitools.eyes.locators.VisualLocator;

import io.qameta.allure.Allure;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VisualActions {

    public static void tapUsingVisualLocator(String locatorName, Eyes eyes, AndroidDriver driver) {
        Allure.step("Nhấn nút: " + locatorName, () -> {
            Map<String, List<Region>> locators = eyes.locate(VisualLocator.name(locatorName));
            List<Region> regions = locators.get(locatorName);
   	    
            if (regions != null && !regions.isEmpty()) {
                Region region = regions.get(0);
                int x = region.getLeft() + region.getWidth() / 2;
                int y = region.getTop() + region.getHeight() / 2;

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tap = new Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
                tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

                driver.perform(Arrays.asList(tap));
            } else {
                throw new RuntimeException("Không tìm thấy phần tử với tên locator: " + locatorName);
            }
        });
    }

    public static void sendKeysUsingVisualLocator(String locatorName, Eyes eyes, AndroidDriver driver, String text) {
        Allure.step("Nhập " + locatorName + ": " + text, () -> {
            Map<String, List<Region>> locators = eyes.locate(VisualLocator.name(locatorName));
            List<Region> regions = locators.get(locatorName);

            if (regions != null && !regions.isEmpty()) {
                Region region = regions.get(0);
                int x = region.getLeft() + region.getWidth() / 2;
                int y = region.getTop() + region.getHeight() / 2;

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tap = new Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
                tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(Arrays.asList(tap));

                WebElement input = (WebElement) driver.switchTo().activeElement();
                input.sendKeys(text);

            } else {
                throw new RuntimeException("Không tìm thấy phần tử với tên locator: " + locatorName);
            }
        });
    }
    
//    public boolean confirmLoginWithApplitools(String expectedUsername, Eyes eyes) {
//        try {
//            // Chụp toàn màn hình hoặc một phần có vùng chứa username
//            TextRegionSettings settings = new TextRegionSettings(expectedUsername);
//            TextRegion region = eyes.locateText(settings);
//
//            if (region != null) {
//                Allure.step("Tìm thấy tên người dùng: " + expectedUsername);
//                return true;
//            } else {
//                Allure.step("Không tìm thấy tên người dùng: " + expectedUsername);
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Allure.step("❌ Gặp lỗi khi xác minh bằng Applitools OCR");
//            return false;
//        }
//    }


}

//import io.appium.java_client.android.AndroidDriver;
//import org.opencv.core.*;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.openqa.selenium.*;
//import org.openqa.selenium.Point;
//import org.openqa.selenium.interactions.PointerInput;
//import org.openqa.selenium.interactions.Sequence;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//import java.time.Duration;
//import java.util.Collections;
//import io.qameta.allure.Allure;
//
//public class VisualActions {
//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }
//
//    private static String captureScreen(AndroidDriver driver, String name) {
//        File screenshot = driver.getScreenshotAs(OutputType.FILE);
//        String savePath = "screenshots/" + name + ".jpg";
//        File destFile = new File(savePath);
//        destFile.getParentFile().mkdirs();
//        try {
//            Files.copy(screenshot.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return savePath;
//    }
//
//    private static Point findImageOnScreen(String screenPath, String templatePath) {
//        Mat screen = Imgcodecs.imread(screenPath);
//        Mat template = Imgcodecs.imread(templatePath);
//        Mat result = new Mat();
//
//        Imgproc.matchTemplate(screen, template, result, Imgproc.TM_CCOEFF_NORMED);
//        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
//
//        if (mmr.maxVal > 0.85) {
//            return new Point((int) mmr.maxLoc.x, (int) mmr.maxLoc.y);
//        } else {
//            return null;
//        }
//    }
//
//    public static void tapByImage(AndroidDriver driver, String templateName) {
//        Allure.step("Nhấn vào ảnh theo template: " + templateName, () -> {
//            String screenPath = captureScreen(driver, "currentScreen");
//            String templatePath = "templates/" + templateName + ".jpg";
//
//            Mat template = Imgcodecs.imread(templatePath);
//            Point location = findImageOnScreen(screenPath, templatePath);
//
//            Allure.step("Tìm vị trí ảnh: " + templateName, () -> {
//                if (location != null) {
//                    int x = location.getX() + template.width() / 2;
//                    int y = location.getY() + template.height() / 2;
//
//                    Allure.step("Ảnh được tìm thấy tại: (" + x + ", " + y + "). Thực hiện tap.", () -> {
//                        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
//                        Sequence tap = new Sequence(finger, 1);
//                        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
//                        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//                        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
//
//                        driver.perform(Collections.singletonList(tap));
//                    });
//                } else {
//                    Allure.step("Không tìm thấy ảnh: " + templateName);
//                }
//            });
//        });
//    }
//
//    public static void sendKeysByImage(AndroidDriver driver, String templateName, String text) {
//    	Allure.step("Nhập dữ liệu vào ô " + templateName + ": " + text, () -> {
//        tapByImage(driver, templateName);
//
//        try {
//            Thread.sleep(800);
//        } catch (InterruptedException ignored) {
//        }
//
//        WebElement activeElement = driver.switchTo().activeElement();
//        activeElement.sendKeys(text);
//    	});
//    }
//
//    public static void checkScreen(AndroidDriver driver, String label) {
//        captureScreen(driver, label.replace(" ", "_"));
//    }
//}
