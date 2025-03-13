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

import com.aventstack.extentreports.util.Assert;

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
    public Object[][] getProductData() throws IOException {
        // Đọc dữ liệu từ file CSV
        String filePath = "DataFile/Product.csv"; 
        List<String[]> csvData = DataReader.getCSVData(filePath, 1);
        Object[][] data = new Object[csvData.size()][1];
        for (int i = 0; i < csvData.size(); i++) {
            data[i][0] = csvData.get(i)[0];
        }
        return data;
    }
    
    @DataProvider(name = "ProducFiltertData")
    public Object[][] getProducFiltertData() throws IOException {
        String filePath = "DataFile/FilterPriceRange.csv"; 
        List<String[]> csvData = DataReader.getCSVData(filePath, 1);
        Object[][] data = new Object[csvData.size()][3];

        for (int i = 0; i < csvData.size(); i++) {
            data[i][0] = csvData.get(i)[0].trim(); // Loại bỏ khoảng trắng
            data[i][1] = csvData.get(i)[1].trim();
            data[i][2] = csvData.get(i)[2].trim();

            // Debug dữ liệu đọc từ CSV
            System.out.println("Dòng " + (i + 1) + ": " + data[i][0] + " | Min: " + data[i][1] + " | Max: " + data[i][2]);
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
	 
	 
	 
//	 @Test(priority = 1, dataProvider = "ProductData", description = "TC05 - Kiểm tra chức năng tìm kiếm")
//	    @Story("Tìm kiếm sản phẩm với tên thành công")
//	    public void TestSearchProduct(String nameProduct) throws InterruptedException, IOException {
//	        SoftAssert softAssert = new SoftAssert();
//
//	        searchPage.sendKeyInputSearch(nameProduct);
//	        searchPage.confirmSearchContent(nameProduct);
//	        searchPage.clickBtnSearch();
//	        
//	        Thread.sleep(2000);
//	        boolean result = searchPage.verifyProductsContainSearchName(nameProduct);
//	        softAssert.assertTrue(result, "Có sản phẩm không phù hợp với từ khóa tìm kiếm : " + nameProduct);
//	        softAssert.assertAll();
//	    }
	 
	 
	 @Test(priority = 2, dataProvider = "ProducFiltertData", description = "TC06 - Kiểm tra chức năng lọc theo khoảng giá")
	 @Story("Lọc theo khoảng giá {1} - {2}")
	 public void TestFilterByPrice(String productName, String min, String max) throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();

	     searchPage.sendKeyInputSearch(productName);
	     
	     searchPage.clickBtnSearch();
	     
	     searchPage.clickFilter();
	     
	     searchPage.filterAS(min, max);

	     // Chờ dữ liệu tải (nếu cần)
	     Thread.sleep(2000);

	     // Kiểm tra sản phẩm có đúng khoảng giá không
	     boolean isPriceValid = searchPage.verifyProductsWithinPriceRange(Integer.parseInt(min), Integer.parseInt(max));

	     softAssert.assertTrue(isPriceValid, "Lỗi: Có sản phẩm không nằm trong khoảng giá mong muốn!");

	     softAssert.assertAll();
	 }

	 

	 @AfterMethod
	 public void TearDown() throws InterruptedException {
	     Thread.sleep(2000);
	     if (driver != null) {
	         driver.quit();
	     }
	 } 
}


