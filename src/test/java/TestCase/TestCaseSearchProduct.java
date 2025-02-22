package TestCase;

import java.io.IOException;
import java.net.MalformedURLException;
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
import Page.SearchPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

@Feature("Kiểm tra chức năng tìm kiếm")
public class TestCaseSearchProduct extends Basic{
    HomePage homePage;
    SearchPage searchPage;
    
    @DataProvider(name = "ProductData")
    public Object[][] getLoginData() throws IOException {
        // Đọc dữ liệu từ file CSV
        String filePath = "DataFile/Product.csv"; 
        List<String[]> csvData = DataReader.getCSVData(filePath, 1);
        Object[][] data = new Object[csvData.size()][1];
        for (int i = 0; i < csvData.size(); i++) {
            data[i][0] = csvData.get(i)[0];
        }
        return data;
    }
    
    
	 @BeforeMethod
	    public void SetUp() throws MalformedURLException {
	        
	        configureAppium(); // Khởi tạo lại driver
	        
	        homePage = new HomePage(driver);
	        searchPage = new SearchPage(driver);

	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        ITestContext context = Reporter.getCurrentTestResult().getTestContext();
	        context.setAttribute("driver", driver);

	        homePage.clickBtnStart();
	        homePage.clickIconSearch();
	    }
	 
	 
	 
	 @Test(priority = 1, dataProvider = "ProductData", description = "TC04 - Kiểm tra chức năng tìm kiếm")
	    @Story("Tìm kiếm sản phẩm thành công")
	    @Step("Tìm kiếm với tên sản phẩm: {0}")
	    public void TestSearchProduct(String nameProduct) throws InterruptedException {
	        SoftAssert softAssert = new SoftAssert();

	        searchPage.sendKeyInputSearch(nameProduct);
	        searchPage.confirmSearchContent(nameProduct);
	        searchPage.clickBtnSearch();
	        
	        Thread.sleep(2000);
	        boolean result = searchPage.verifyProductsContainSearchName(nameProduct);
	        softAssert.assertTrue(result, "Không tìm thấy sản phẩm chứa tên: " + nameProduct);
	        
	        // Kiểm tra tất cả các assertion
	        softAssert.assertAll();
	    }
	 
//	 @Test(priority = 2,description = "TC04 - Kiểm tra chức năng lọc theo giá")
//	    @Story("Lọc sản phẩm thành công")
//	    public void TestSearchProduct() throws InterruptedException {
//	        SoftAssert softAssert = new SoftAssert();
//
//	        searchPage.sendKeyInputSearch("Áo thun");
//	        searchPage.clickBtnSearch();
//	        searchPage.isOptionPriceSelected();
//	      
//	    }
	 @AfterMethod
	    public void TearDown() throws InterruptedException {
	        Thread.sleep(2000);
	        if (driver != null) {
	            System.out.println("Đóng hoàn toàn Appium driver...");
	            driver.quit(); 
	        }
	    }
}


