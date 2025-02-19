package TestCase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Base.Basic;
import Data.DataReader;
import Page.HomePage;
import Page.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

@Feature("Kiểm tra chức năng Đăng nhập")
public class TestCaseLogin extends Basic {
    HomePage homePage;
    LoginPage loginPage;

    @DataProvider(name = "ValidLoginData")
    public Object[][] getValidLoginData() throws IOException {
        String filePath = "DataFile/LoginCSV.csv";
        List<String[]> csvData = DataReader.getCSVData(filePath, 1); // Bỏ qua tiêu đề

        List<Object[]> validData = new ArrayList<>();
        for (String[] row : csvData) {
            if (!row[2].isEmpty()) { // Chỉ lấy dữ liệu có username
                validData.add(new Object[]{row[0], row[1], row[2]});
            }
        }
        return validData.toArray(new Object[0][]);
    }

    @DataProvider(name = "InvalidLoginData")
    public Object[][] getInvalidLoginData() throws IOException {
        String filePath = "DataFile/LoginCSV.csv";
        List<String[]> csvData = DataReader.getCSVData(filePath, 1); // Bỏ qua tiêu đề

        List<Object[]> invalidData = new ArrayList<>();
        for (String[] row : csvData) {
            if (row[2].isEmpty()) { // Chỉ lấy dữ liệu không có username
                invalidData.add(new Object[]{row[0], row[1]});
            }
        }
        return invalidData.toArray(new Object[0][]);
    }


    @BeforeMethod
    public void SetUp() throws MalformedURLException {
        
        configureAppium(); // Khởi tạo lại driver
        
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        ITestContext context = Reporter.getCurrentTestResult().getTestContext();
        context.setAttribute("driver", driver);

        homePage.clickBtnStart();
        homePage.clickLoginAS();
    }


    @Test(priority = 1, dataProvider = "ValidLoginData", description = "TC01 - Kiểm tra chức năng đăng nhập bằng số điện thoại với thông tin hợp lệ")
    @Story("Đăng nhập bằng số điện thoại thành công")
    @Step("Login with phoneNumber: {0}, password: {1}, username: {2}")
    public void TestLoginWithPhoneNumber(String phoneNumber, String password, String username) {
        SoftAssert softAssert = new SoftAssert();
        loginPage.loginAS(phoneNumber, password, softAssert);
        loginPage.verifyErrorMessagesInSpans(softAssert, phoneNumber, password);
        loginPage.clickBtnLogin();
        try {
            Thread.sleep(3000);
            homePage.clickBtnBack();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean isLoginSuccessful  = homePage.confirmLogin(username);
        softAssert.assertTrue(isLoginSuccessful, "Xác nhận đăng nhập thất bại");

        // Kiểm tra tất cả các assertion
        softAssert.assertAll();
    }
    
    
    @Test(priority = 2, dataProvider = "InvalidLoginData", description = "TC02 - Kiểm tra chức năng đăng nhập thất bại")
    @Story("Đăng nhập thất bại")
    @Step("Login with phoneNumber: {phoneNumber}, password: {password}")
    public void TestLoginFail(String phoneNumber, String password) throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        String notification = "Tài khoản hoặc mật khẩu đăng nhập không chính xác. Vui lòng thử lại.";
        // Thực hiện đăng nhập với thông tin sai
        loginPage.loginAS(phoneNumber, password, softAssert);
        loginPage.verifyErrorMessagesInSpans(softAssert, phoneNumber, password);
        loginPage.clickBtnLogin();
        Thread.sleep(500);
        // Xác nhận đăng nhập không thành công
        boolean isLoginFailed = loginPage.confirmLoginFaild(notification);
        softAssert.assertTrue(isLoginFailed, "Thông báo lỗi đăng nhập thất bại không đúng");

        // Kiểm tra tất cả các assertion
        softAssert.assertAll();
    }


    @AfterMethod
    public void TearDown() throws InterruptedException {
        Thread.sleep(2000);
        if (driver != null) {
            System.out.println("Đóng hoàn toàn Appium driver...");
            driver.quit(); // Đóng driver hoàn toàn
        }
    }
}
