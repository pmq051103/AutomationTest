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
import utils.listeners.Utils;

@Feature("Kiểm tra chức năng tìm kiếm")
public class TestCaseSearchProduct extends Basic{
    HomePage homePage;
    SearchPage searchPage;
    Utils utils;
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
    
    @DataProvider(name = "ProducFiltertData_TC12")
    public Object[][] getDataForTC11() throws IOException {
        String filePath = "DataFile/FilterPriceRange.csv"; 
        List<String[]> csvData = DataReader.getCSVData(filePath, 1);
        // Lấy dòng đầu tiên
        if (csvData.size() >= 1) {
            return new Object[][] { {
                csvData.get(0)[0].trim(),
                csvData.get(0)[1].trim(),
                csvData.get(0)[2].trim()
            } };
        }
        return new Object[0][];
    }

    @DataProvider(name = "ProducFiltertData_TC11")
    public Object[][] getDataForTC12() throws IOException {
        String filePath = "DataFile/FilterPriceRange.csv"; 
        List<String[]> csvData = DataReader.getCSVData(filePath, 1);
        // Lấy dòng thứ hai
        if (csvData.size() >= 2) {
            return new Object[][] { {
                csvData.get(1)[0].trim(),
                csvData.get(1)[1].trim(),
                csvData.get(1)[2].trim()
            } };
        }
        return new Object[0][];
    }

    
	 @BeforeMethod
	    public void SetUp() throws MalformedURLException {
	        
	        configureAppium(); // Khởi tạo lại driver
	        
	        homePage = new HomePage(driver);
	        searchPage = new SearchPage(driver);
	        utils = new Utils(driver);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        ITestContext context = Reporter.getCurrentTestResult().getTestContext();
	        context.setAttribute("driver", driver);

	        homePage.clickBtnBack();
	    }
	 
	 
	 
	 @Test(priority = 1, dataProvider = "ProductData", description = "TC05 - Xác minh chức năng tìm kiếm sản phẩm với tên thành công")
	    @Story("Tìm kiếm sản phẩm với tên thành công")
	    public void TestSearchProduct(String nameProduct) throws InterruptedException, IOException {
	        SoftAssert softAssert = new SoftAssert();
			homePage.clickIconSearch();
	        searchPage.sendKeyInputSearch(nameProduct);
	        searchPage.confirmSearchContent(nameProduct);
	        searchPage.clickBtnSearch();
	        
	        Thread.sleep(2000);
	        boolean result = searchPage.verifyProductsSearchByName(nameProduct);
	        softAssert.assertTrue(result, "Có sản phẩm không phù hợp với từ khóa tìm kiếm : " + nameProduct);
	        softAssert.assertAll();
	    }
	 
	 @Test(priority = 2, description = "TC06 - Xác minh chức năng tìm kiếm sản phẩm với nội dung tìm kiếm không hợp lệ")
	    @Story("Tìm kiếm sản phẩm với tên sản phẩm không hợp lệ")
	    public void TestSearchProduct_WithInvalidKeyword() throws InterruptedException, IOException {
	        SoftAssert softAssert = new SoftAssert();
			homePage.clickIconSearch();
			String nameProduct = "dhsjkdj-_!\";\"?₫(";
			String message = "Không tìm thấy kết quả nào";
	        searchPage.sendKeyInputSearch(nameProduct);
	        searchPage.confirmSearchContent(nameProduct);
	        searchPage.clickBtnSearch();
	        
	        
	        Thread.sleep(2000);
	        boolean result = searchPage.verifyEmptySearchResultSpan(message);
	        softAssert.assertTrue(result, "Có sản phẩm không phù hợp với từ khóa tìm kiếm : " + nameProduct);
	        softAssert.assertAll();
	    }
	 
	 	@Test(priority = 3 , description = "TC07 - Xác minh chức năng tìm kiếm bằng gợi ý")
	 	@Story("Tìm kiếm sản phẩm qua gợi ý")
	    public void TestSearchProductWithSuggestion() throws InterruptedException, IOException {
	        SoftAssert softAssert = new SoftAssert();
			homePage.clickIconSearch();
			String suggestionText = searchPage.selectRandomSuggest();
	        
	        Thread.sleep(2000);
	        boolean result = searchPage.verifyFirstProductMatchesSuggestion(suggestionText);

	        // Bước 5: Kiểm tra kết quả
	        softAssert.assertTrue(result, "Có sản phẩm không phù hợp với gợi ý tìm kiếm");
	        softAssert.assertAll();
	    }
	 
	 
	 
	 
	 @Test(priority = 4, description = "TC08 - Xác minh chức năng tìm kiếm bằng hình ảnh")
	    @Story("Tìm kiếm sản phẩm bằng hình ảnh")
	    public void TestSearchProductByImage() throws InterruptedException, IOException {
	        SoftAssert softAssert = new SoftAssert();
	        homePage.clickBtnSearchByImage();
	        searchPage.chooseImage();	        
	        Thread.sleep(2000);
	        boolean result = searchPage.verifyProductsSearchByImage("ProductByImage");
	        softAssert.assertTrue(result, "Có sản phẩm không phù hợp với hình ảnh tìm kiếm");
	        softAssert.assertAll();
	    }
	 
	 @Test(priority = 5, description = "TC09 - Xác minh chức năng lọc sản phẩm theo giá tăng dần")
	 @Story("Lọc sản phẩm theo giá tăng dần")
	 public void TestFilterProductsByAscendingPrice() throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();
	     String nameProduct = " Túi xách nữ";
	     homePage.clickIconSearch();
	     
	     searchPage.sendKeyInputSearch(nameProduct);
	     searchPage.confirmSearchContent(nameProduct);
	     searchPage.clickBtnSearch();
	     Thread.sleep(1000);
	     searchPage.clickOptionPriceAscending();
	     Thread.sleep(4000);
	     boolean result = searchPage.verifyProductsSortedByAscendingPrice();

	     softAssert.assertTrue(result, "Test thất bại: Danh sách sản phẩm không được sắp xếp theo giá tăng dần!");
	     softAssert.assertAll();	    
	 }
	 
	 @Test(priority = 6, description = "TC10 - Xác minh chức năng lọc sản phẩm theo giá giảm dần")
	 @Story("Lọc sản phẩm theo giá giảm dần")
	 public void TestFilterProductsByDescendingPrice() throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();
	     String nameProduct = "Túi xách nữ";
	     
	     homePage.clickIconSearch();
	     searchPage.sendKeyInputSearch(nameProduct);
	     searchPage.confirmSearchContent(nameProduct);
	     searchPage.clickBtnSearch();
	     
	     Thread.sleep(1000);
	     searchPage.clickOptionPriceDescending(); // Chọn sắp xếp theo giá giảm dần
	     Thread.sleep(4000);

	     boolean result = searchPage.verifyProductsSortedByDescendingPrice();

	     softAssert.assertTrue(result, "Test thất bại: Danh sách sản phẩm không được sắp xếp theo giá giảm dần!");
	     softAssert.assertAll();
	 }

	 
	 @Test(priority = 7, dataProvider = "ProducFiltertData_TC11", description = "TC11 - Xác minh chức năng lọc sản phẩm theo khoảng giá có sẵn")
	 @Story("Lọc theo khoảng giá có sẵn")
	 public void TestFilterByPrice(String productName, String min, String max) throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();
		homePage.clickIconSearch();
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
	 
	 @Test(priority = 8, dataProvider = "ProducFiltertData_TC12", description = "TC12 - Xác minh chức năng lọc sản phẩm theo khoảng Giá nhập từ ô input")
	 @Story("Lọc theo khoảng giá nhập từ ô input")
	 public void TestFilterByPriceInput(String productName, String min, String max) throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();
		homePage.clickIconSearch();
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
	 
	 @Test(priority = 9, description = "TC13 - Xác minh chức năng lọc sản phẩm khi điều kiện khoảng giá không hợp lệ")
	 @Story("Lọc theo khoảng giá không hợp lệ")
	 public void TestFilterByPriceInValid() throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();
	     String min = "1000000";
	     String max = "100000";
	     String productName = "Áo thun nam";
	     String toast = "Giá tối thiểu phải nhỏ hơn giá tối đa!";
		homePage.clickIconSearch();
	     searchPage.sendKeyInputSearch(productName);
	     
	     searchPage.clickBtnSearch();
	     
	     searchPage.clickFilter();
	     
	     searchPage.filterAS(min, max);

	     // Chờ dữ liệu tải (nếu cần)
	     Thread.sleep(2000);

	     // Kiểm tra sản phẩm có đúng khoảng giá không
	     boolean isPriceValid = utils.confirmToast(toast);

	     softAssert.assertTrue(isPriceValid, "Lỗi: Có sản phẩm không nằm trong khoảng giá mong muốn!");

	     softAssert.assertAll();
	 }

	 @Test(priority = 8, description = "TC14 - Xác minh chức năng lọc sản phẩm theo nơi bán")
	 @Story("Lọc sản phẩm theo nơi bán")
	 public void TestFilterProductsByLocation() throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();
	     String nameProduct = "Bánh tráng trộn";
	     // Bước 1: Tìm kiếm sản phẩm
	     homePage.clickIconSearch();
	     searchPage.sendKeyInputSearch(nameProduct);
	     searchPage.confirmSearchContent(nameProduct);
	     searchPage.clickBtnSearch();
	     Thread.sleep(1000);
	     searchPage.clickFilter();

	     // Bước 2: Chọn bộ lọc theo nơi bán
	     String location = searchPage.clickOptionLocation();
	     Thread.sleep(2000);

	     // Bước 3: Kiểm tra sản phẩm đã được lọc đúng theo nơi bán
	     boolean result = searchPage.verifyProductsWithWhereBuy(location);

	     // Xác nhận test case thành công nếu tất cả sản phẩm đều có nơi bán đúng
	     softAssert.assertTrue(result, "Test thất bại: Sản phẩm không đúng nơi bán được chọn!");
	     softAssert.assertAll();
	 }
	 
	 @Test(priority = 9, description = "TC15 - Xác minh chức năng lọc sản phẩm theo đánh giá")
	 @Story("Lọc sản phẩm theo đánh giá")
	 public void TestFilterProductsByRating() throws InterruptedException {
	     SoftAssert softAssert = new SoftAssert();
	     String nameProduct = "Áo thun nam";
	     
	     // Bước 1: Tìm kiếm sản phẩm
	     homePage.clickIconSearch();
	     searchPage.sendKeyInputSearch(nameProduct);
	     searchPage.confirmSearchContent(nameProduct);
	     searchPage.clickBtnSearch();
	     Thread.sleep(1000);
	     searchPage.clickFilter();
	     // Bước 2: Chọn bộ lọc theo đánh giá
	    String feedBackStr= searchPage.clickFilterFeedback();
	     Thread.sleep(2000);

	     boolean result = searchPage.verifyProductsWithinRating(feedBackStr);

	     softAssert.assertTrue(result, "Test thất bại: Sản phẩm không đúng đánh giá đã chọn!");
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


