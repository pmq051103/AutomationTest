package Page;

import java.time.Duration;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class LoginPage{
    private AndroidDriver driver;
    private WebDriverWait wait;
    private Actions ac;

    @AndroidFindBy(xpath = "(//android.widget.EditText[@resource-id=\"com.shopee.vn:id/cret_edit_text\"])[1]")
    WebElement inputPhoneOrEmail;

    @AndroidFindBy(xpath = "(//android.widget.EditText[@resource-id=\"com.shopee.vn:id/cret_edit_text\"])[2]")
    WebElement inputPassword;

    @AndroidFindBy(id = "com.shopee.vn:id/cret_show_hide_text_image_view")
    WebElement iconEge;

    @AndroidFindBy(id = "com.shopee.vn:id/btnLogin")
    WebElement btnLogin;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đăng nhập với Google\")")
    WebElement btnLoginWithGoogle;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đăng nhập với Facebook\")")
    WebElement btnLoginWithFacebook;
    
    @AndroidFindBy(id = "com.shopee.vn:id/cret_show_hide_text_image_view")
    WebElement iconEye;
    
    @AndroidFindBy(className = "android.widget.Toast")
    WebElement toastNotification;
    
    

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ac = new Actions(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    
    public LoginPage sendKeyPhoneNumber(String phoneNumber) {
    	Allure.step("Nhập số điện thoại: " + phoneNumber);
        wait.until(ExpectedConditions.visibilityOf(inputPhoneOrEmail)).sendKeys(phoneNumber);
        return this;
    }

    public LoginPage sendKeyPassWord(String password) {
    	Allure.step("Nhập mật khẩu: " + password);
        wait.until(ExpectedConditions.visibilityOf(inputPassword)).sendKeys(password);
        return this;
    }

    public LoginPage clickBtnLogin() {
    	Allure.step("Nhấn nút đăng nhập");
        btnLogin.click();
        return this;
    }
    
    public LoginPage clickIconEye() {
    	iconEge.click();
    	return this;
    }
    
    
    @Step("Login with username: {phoneNumber}, password: {passWord}")
    public LoginPage loginAS(String phoneNumber, String passWord, SoftAssert softAssert) {
        sendKeyPhoneNumber(phoneNumber);
        sendKeyPassWord(passWord);
        clickIconEye();
        return this;
    }
    
    private void checkInputValue(String fieldName, String actualValue, String expectedValue, SoftAssert softAssert) {
        Allure.step("Kiểm tra " + fieldName, () -> {
            if (!expectedValue.equals(actualValue)) {
                Allure.addAttachment("Thông báo lỗi " + fieldName, "Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            } else {
                Allure.step(fieldName + " khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            }
            softAssert.assertEquals(actualValue, expectedValue, 
                fieldName + " không khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
        });
    }

    
    public void verifyInputValue(SoftAssert softAssert, String expectedPhoneOrEmail, String expectedPassWord){
        String span_PhoneOrEmail = inputPhoneOrEmail.getText().replaceAll("[^0-9]", "").replaceAll("\\(\\+84\\)\\s*", "").replaceAll("\\s+", "");
        String span_Password = inputPassword.getText();
    	// Kiểm tra và log từng thông báo
    	checkInputValue("Phone Or Email", span_PhoneOrEmail, expectedPhoneOrEmail, softAssert);
    	checkInputValue("Password", span_Password, expectedPassWord, softAssert);
    }
    
    @Step("Kiểm tra kết quả đăng nhập")
    public boolean confirmLoginFaild(String toastNotificationActual) {
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
                System.err.println("Lỗi khi lấy Toast: " + e.getMessage());
                Allure.addAttachment("Lỗi khi lấy Toast", e.getMessage());
                return false;
            }
        });
    }


    
    /**
     * Lấy OTP từ tin nhắn SMS bằng ADB
     */
    public String getOtpFromSms() {
        try {
            // Chạy lệnh ADB để lấy danh sách tin nhắn SMS
            String otpMessage = driver.executeScript("mobile: shell", ImmutableMap.of(
                "command", "content query --uri content://sms/inbox --projection body",
                "includeStderr", true,
                "timeout", 5000
            )).toString();

            // Trích xuất mã OTP từ tin nhắn
            Pattern pattern = Pattern.compile("\\d{6}"); // OTP 6 chữ số
            Matcher matcher = pattern.matcher(otpMessage);
            if (matcher.find()) {
                return matcher.group(0);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy OTP từ SMS: " + e.getMessage());
        }
        return null;
    }

    /**
     * Nhập OTP vào trường input
     */
    public void enterOtp() {
        String otpCode = getOtpFromSms();
        if (otpCode != null) {
            System.out.println("OTP: " + otpCode);
            try {
                WebElement otpInput = driver.findElement(By.id("id_truong_otp"));
                otpInput.sendKeys(otpCode);
            } catch (Exception e) {
                System.err.println("Lỗi khi nhập OTP: " + e.getMessage());
            }
        } else {
            System.err.println("Không lấy được OTP.");
        }
    }

    /**
     * Click vào nút đăng ký
     */
    public LoginPage clickBtnRegister() {
        try {
            clickAtCoordinates(640, 1370);
        } catch (Exception e) {
            System.err.println("Cannot click the register button: " + e.getMessage());
        }
        return this;
    }

    /**
     * Click vào vị trí bất kỳ trên màn hình bằng toạ độ
     */
    private void clickAtCoordinates(int x, int y) {
        try {
            // Tạo một PointerInput
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

            // Tạo chuỗi hành động
            Sequence tap = new Sequence(finger, 0);
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // Thực hiện hành động
            driver.perform(Collections.singletonList(tap));
            System.out.println("Đã click vào toạ độ (" + x + ", " + y + ")");
        } catch (Exception e) {
            System.err.println("Lỗi khi click vào toạ độ: " + e.getMessage());
        }
    }
    
    public LoginPage loginA(String phoneNumber, String passWord) {
        sendKeyPhoneNumber(phoneNumber);
        sendKeyPassWord(passWord);
        clickBtnLogin();
        return this;
    }
}
