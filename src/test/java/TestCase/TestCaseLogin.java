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
        return readLoginData(0); 
    }

    @DataProvider(name = "InvalidLoginData1")
    public Object[][] getInvalidLoginData1() throws IOException {
        return readLoginData(1);
    }

    @DataProvider(name = "InvalidLoginData2")
    public Object[][] getInvalidLoginData2() throws IOException {
        return readLoginData(2);
    }


    private Object[][] readLoginData(int typeFilter) throws IOException {
        String filePath = "DataFile/Login.csv";
        List<String[]> csvData = DataReader.getCSVData(filePath, 1); 

        List<Object[]> filteredData = new ArrayList<>();

        for (String[] row : csvData) {
            int type = Integer.parseInt(row[3]);
            if (type == typeFilter) {
                filteredData.add(new Object[]{row[0], row[1], row[2]});
            }
        }
        return filteredData.toArray(new Object[0][]);
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


    @Test(priority = 1, dataProvider = "ValidLoginData", description = "TC01 - Kiểm tra đăng nhập thành công")
    @Story("Đăng nhập bằng số điện thoại thành công")
    @Step("Login with phoneNumber: {0}, password: {1}, username: {2}")
    public void TestLoginWithPhoneNumber(String phoneNumber, String password, String expectedUsername) {
        SoftAssert softAssert = new SoftAssert();
        
        loginPage.loginAS(phoneNumber, password, softAssert);
        loginPage.clickBtnLogin();
        
        try {
            Thread.sleep(3000);
            homePage.clickBtnBack();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean isLoginSuccessful = homePage.confirmLogin(expectedUsername);
        softAssert.assertTrue(isLoginSuccessful, "Xác nhận đăng nhập thất bại");

        softAssert.assertAll();
    }


    @Test(priority = 2, dataProvider = "InvalidLoginData1", description = "TC02 - Kiểm tra đăng nhập thất bại")
    @Story("Đăng nhập thất bại với tài khoản mật khẩu không đúng")
    @Step("Login with phoneNumber: {0}, password: {1}")
    public void TestLoginFail(String phoneNumber, String password, String expectedNotification) throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        
        loginPage.loginAS(phoneNumber, password, softAssert);
        loginPage.clickBtnLogin();
        Thread.sleep(500);

        boolean isLoginFailed = loginPage.confirmLoginFaild(expectedNotification);
        softAssert.assertTrue(isLoginFailed, "Thông báo lỗi đăng nhập không đúng");

        softAssert.assertAll();
    }



    @Test(priority = 3, dataProvider = "InvalidLoginData2", description = "TC03 - Kiểm tra đăng nhập thất bại với tài khoản < 9 kí tự")
    @Story("Đăng nhập thất bại với tài khoản < 9 kí tự")
    @Step("Login with phoneNumber: {0}, password: {1}")
    public void TestLoginFail_AccountLengthLessThan8(String phoneNumber, String password, String expectedMessage) throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        
        loginPage.loginAS(phoneNumber, password, softAssert);
        loginPage.clickBtnLogin();
        Thread.sleep(500);

        boolean isLoginFailed = loginPage.confirmLoginFaild(expectedMessage);
        softAssert.assertTrue(isLoginFailed, "Thông báo lỗi đăng nhập không đúng");

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
