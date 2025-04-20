package TestCase;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Base.Basic;
import Page.AddProductPage;
import Page.CartPage;
import Page.FavoritePage;
import Page.HomePage;
import Page.LoginPage;
import Page.ProductDetailPage;
import Page.SearchPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Feature("Kiểm tra chức năng thêm và xóa sản phẩm khỏi danh sách yêu thích")
public class TestCaseAddFavorite extends Basic{
	HomePage homePage;
	SearchPage searchPage;
	LoginPage loginPage;
	SoftAssert softAssert;
	ProductDetailPage productDetailPage;
	FavoritePage favoritePage;
	CartPage cartPage;
	
	@BeforeMethod
    public void SetUp(ITestResult result) throws MalformedURLException, InterruptedException {
        
        configureAppium(); // Khởi tạo lại driver
        
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        searchPage = new SearchPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        favoritePage = new FavoritePage(driver);
        cartPage = new CartPage(driver);
        
        
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        ITestContext context = Reporter.getCurrentTestResult().getTestContext();
        context.setAttribute("driver", driver);
        
        homePage.clickBtnBack();
        int priority = result.getMethod().getPriority();

        if (priority != 1) {
            homePage.clickLoginAS();
            loginPage.loginA("0333085521", "Quang0511@");
            Thread.sleep(2000);
            homePage.clickBtnBack();
            Thread.sleep(1500);
            if (priority != 3 && priority != 5 && priority !=6) {
                homePage.clickBtnHome();
                if(priority != 5 && priority !=6) {
                	homePage.clickIconSearch();
                }
            }
        }   
    }
	
	
	@Test(priority = 1, description = "TC24 - Xác minh chức năng thêm vào danh sách yêu thích khi chưa đăng nhập")
	@Story("Chuyển hướng đến trang đăng nhập khi chưa đăng nhập mà bấm Add to Favorite")
	public void TestRedirectToLoginWhenAddingFavoriteWithoutLogin() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();

	    String productSearch = "tai nghe bluetooth";
	    homePage.clickIconSearch();
	    searchPage.searchByNameAS(productSearch);
	    searchPage.clickProduct(0);
	    Thread.sleep(1500);

	    productDetailPage.clickBtnLike(); 
	    Thread.sleep(1000);
	    
	    boolean isLoginPageDisplayed = loginPage.isLoginPageDisplayed();
	    softAssert.assertTrue(isLoginPageDisplayed, "Không chuyển đến trang đăng nhập!");

	    softAssert.assertAll();
	}
	
	@Test(priority = 2, description = "TC25 - Xác minh chức năng thêm vào danh sách yêu thích thành công")
	@Story("Thêm sản phẩm vào danh sách yêu thích khi đã đăng nhập")
	public void TestAddToFavoriteSuccessfully() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();
	    String productSearch = "Iphone";
	    String toastMessageExpected = "Thêm thành công vào mục Đã thích!";
	    
	    // Tìm kiếm và chọn sản phẩm
	    searchPage.searchByNameAS(productSearch);
	    searchPage.clickProduct(1);
	    Thread.sleep(1500);
	    
	    String productExpected  = productDetailPage.getProductName();
	    
	    // Nhấn "Thích"
	    productDetailPage.clickBtnLike(); 
	    Thread.sleep(1000);

	    // Kiểm tra thông báo toast
	    boolean checkToast = productDetailPage.verifyToast(toastMessageExpected,softAssert);
	    softAssert.assertTrue(checkToast, "Thông báo Toast không đúng hoặc không hiển thị!");
	    
	    // Nhấn vào nút "Xem danh sách yêu thích"
	    productDetailPage.clickBtnView();
	    Thread.sleep(1000);

	    // Kiểm tra sản phẩm có trong danh sách yêu thích không
	    boolean isProductInFavorite = favoritePage.isProductInFavorite(productExpected);
	    softAssert.assertTrue(isProductInFavorite, "Sản phẩm không có trong danh sách yêu thích!");

	    // Tổng hợp và báo lỗi nếu có
	    softAssert.assertAll();
	}
	
	@Test(priority = 3, description = "TC26 - Xác minh chức năng xóa 1 sản phẩm khỏi danh sách yêu thích thành công")
	@Story("Xóa sản phẩm khỏi danh sách yêu thích khi đã đăng nhập")
	public void TestRemoveFromFavoriteSuccessfully() throws InterruptedException {  
		SoftAssert softAssert = new SoftAssert();
	    String toastMessageExpected = "Bỏ thích";
	    homePage.clickFavoriteAS();
	    Thread.sleep(1000);
	    String productToRemove = favoritePage.getName(); 

	    // Truy cập vào danh sách yêu thích
	    favoritePage.unlikeAS();
	    Thread.sleep(800);

	    boolean checkToast = favoritePage.confirmToast(toastMessageExpected);
	    softAssert.assertTrue(checkToast, "Thông báo Toast không đúng hoặc không hiển thị!");

	    // Kiểm tra sản phẩm đã bị xóa thành công chưa
	    boolean isProductDeleted = favoritePage.isProductDeleted(productToRemove);
	    softAssert.assertTrue(isProductDeleted, "Sản phẩm vẫn còn trong danh sách yêu thích sau khi xóa!");

	    // Tổng hợp kết quả kiểm tra
	    softAssert.assertAll();
	}


	@Test(priority = 4, description = "TC27 - Xác minh chức năng thêm sản phẩm từ giỏ hàng vào danh sách yêu thích")
	@Story("Thêm sản phẩm từ giỏ hàng vào danh sách yêu thích")
	public void TestAddToFavoriteFromCartSuccessfully() throws InterruptedException {  
	    SoftAssert softAssert = new SoftAssert();
	    
	    // Mở giỏ hàng
	    homePage.clickIconCart();
	    Thread.sleep(1000);
	    String productName = "Ringke Silicone Magnetic Vỏ Bảo Vệ Silicon Mềm Nhẹ Cho iPhone 16 Pro Max 16 Pro";
	    
	    cartPage.clickBtnEdit();
	    cartPage.verifyAndCheckProduct(productName);
	    cartPage.clickBtnAddFavorite();
	    
	    Thread.sleep(800);
	    boolean checkToast = cartPage.confirmToast("Đã chuyển đến trang Đã thích");
	    softAssert.assertTrue(checkToast, "Toast không hiển thị hoặc không đúng!");
	    Thread.sleep(1000);
	    
	    homePage.clickBtnBack();
	    homePage.clickBtnMe();
	    homePage.clickFavoriteAS();
	    Thread.sleep(1000);
	    
	    // Kiểm tra sản phẩm có trong danh sách yêu thích không
	    boolean isProductInFavorites = favoritePage.isProductInFavorites(productName);
	    softAssert.assertTrue(isProductInFavorites, "Sản phẩm không có trong danh sách yêu thích!");

	    // Tổng hợp kết quả kiểm tra
	    softAssert.assertAll();
	}

	@Test(priority = 5, description = "TC28 - Xác minh chức năng lọc sản phẩm yêu thích theo giảm giá")
	@Story("Lọc sản phẩm theo giảm giá")
	public void TestFilterProductByPriceSuccessfully() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();
	    homePage.clickFavoriteAS();
	    favoritePage.clickFilterDiscount();
	    Thread.sleep(2000);

	    // Chọn bộ lọc giá
	    boolean isPriceFilterCorrect = favoritePage.verifyProductsFilterWithDiscount();

	    softAssert.assertTrue(isPriceFilterCorrect, "Lọc danh sách yêu thích theo giảm giá không chính xác!");

	    // Tổng hợp kết quả kiểm tra
	    softAssert.assertAll();
	}

	
	
	@Test(priority = 6, description = "TC29 - Xác minh chức năng xóa tất cả sản phẩm khỏi danh sách yêu thích thành công")
	@Story("Xóa tất cả sản phẩm khỏi danh sách yêu thích")
	public void TestRemoveAllFromFavoriteSuccessfully() throws InterruptedException {  
	    SoftAssert softAssert = new SoftAssert();
	    String toastMessageExpected = "Bạn chưa chọn thích sản phẩm nào";
	    
	    homePage.clickFavoriteAS();
	    Thread.sleep(1000);
	    favoritePage.clickBtnEdit();
	    

	    // Chọn tất cả sản phẩm để bỏ thích
	    favoritePage.selectAllCheckBoxUntilEmpty();
	    Thread.sleep(800);

	    // Nhấn nút bỏ thích tất cả
	    favoritePage.clickBtnUnlikeAll();
	    Thread.sleep(1000);
	    favoritePage.clickBtnConfirm();
	    
	    // Xác nhận toast message
	    boolean checkToast = favoritePage.confirmSpanProductEmpty(toastMessageExpected);
	    softAssert.assertTrue(checkToast, "Thông báo không đúng hoặc không hiển thị!");
	    // Tổng hợp kết quả kiểm tra
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
