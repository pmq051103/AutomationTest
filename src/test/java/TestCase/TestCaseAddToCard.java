package TestCase;

import java.net.MalformedURLException;

import java.util.Random;
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

@Feature("Kiểm tra chức năng giỏ hàng")
public class TestCaseAddToCard extends Basic{
	HomePage homePage;
	CartPage CartPage;
	LoginPage loginPage;
	SoftAssert softAssert;
	SearchPage searchPage;
	FavoritePage favoritePage;
	
	
	ProductDetailPage productDetailPage;
	String productName1 = "Điện Thoại Samsung Galaxy A55 8GB/128GB";
	@BeforeMethod
    public void SetUp(ITestResult result) throws MalformedURLException, InterruptedException {
        
        configureAppium(); // Khởi tạo lại driver
        
        homePage = new HomePage(driver);
        CartPage = new CartPage(driver);
        loginPage = new LoginPage(driver);
        searchPage = new SearchPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        favoritePage = new FavoritePage(driver);
        
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
            if (priority != 8) {
                homePage.clickBtnHome();
            }
        }
        if(priority != 6 && priority != 7 && priority !=4 && priority != 8) {
        	 homePage.clickIconSearch();
        }
    }
	
	@Test(priority = 1, description = "TC16 - Xác minh chức năng thêm sản phẩm vào giỏ hàng khi chưa đăng nhập")
	@Story("Chuyển hướng đến trang đăng nhập khi chưa đăng nhập mà bấm Add to Cart")
	public void TestRedirectToLoginWhenNotLoggedIn() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();

	    String productSearch = "Xí muội";

	    searchPage.searchByNameAS(productSearch);
	    searchPage.clickProduct(0);
	    Thread.sleep(1500);

	    productDetailPage.addToCardAS(); 
	    Thread.sleep(1000);
	    boolean isLoginPageDisplayed = loginPage.isLoginPageDisplayed();
	    softAssert.assertTrue(isLoginPageDisplayed, "Không chuyển đến trang đăng nhập!");

	    softAssert.assertAll();
	}

	@Test(priority = 2 , description = "TC17 - Xác minh chức năng thêm sản phẩm vào giỏ hàng khi nhập quá số lượng tồn kho")
	@Story("Thêm sản phẩm vào giỏ hàng khi nhập quá số lượng tồn kho")
	public void TestAddProductMaxStock() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();
	    
	    String productSearch = "Xí muội";
	    String message = "Số lượng bạn chọn đã đạt mức tối đa của sản phẩm này";

	    searchPage.searchByNameAS(productSearch);
	    searchPage.clickProduct(0);
	    Thread.sleep(1500);

	    String productName = productDetailPage.getProductName();
	    productDetailPage.addToCardAS();
	    Thread.sleep(1500);
	    int quantity = productDetailPage.getStockQuantity(); 
	    productDetailPage.sendkeyInputQuantity(quantity);
	    Thread.sleep(1000);
	    
	    boolean result = productDetailPage.verifyMessage(message, softAssert);
	    softAssert.assertTrue(result, "Thông báo hiển thị không đúng!");

	    softAssert.assertAll();
	}
	
	
	@Test(priority = 3 , description = "TC18 - Xác minh chức năng thêm sản phẩm vào giỏ hàng thành công")
	@Story("Thêm sản phẩm vào giỏ hàng thành công")
	public void TestAddProductToCart() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();
	    
	    String productSearch = "Xí muội";
	    int quantity = new Random().nextInt(5) + 1; 

	    searchPage.searchByNameAS(productSearch);
	    searchPage.clickProduct(0);
	    Thread.sleep(1500);

	    String productName = productDetailPage.getProductName();
	    productDetailPage.addToCardAS();
	    productDetailPage.sendkeyInputQuantity(quantity);
	    Thread.sleep(1500);
	    String option = productDetailPage.getOptionSelected();
	    System.out.println("Tên: " + productName + ", Option: " + option);

	    productDetailPage.clickBtnAdd();
	    Thread.sleep(1000);

	    boolean toastResult = productDetailPage.checkToastMessage("Đã thêm vào giỏ");
	    softAssert.assertTrue(toastResult, "Thông báo 'Đã thêm vào giỏ' không hiển thị!");
	    productDetailPage.clickBtnCart();
	    
	    boolean cartResult = CartPage.verifyProductDetails(productName, option, quantity);
	    softAssert.assertTrue(cartResult, "Thông tin sản phẩm trong giỏ hàng không khớp!");

	    softAssert.assertAll();
	}
	
	

	
	@Test(priority = 4, description = "TC19 - Xác minh chức năng thêm sản phẩm đã có sẵn trong giỏ hàng")
	@Story("Thêm sản phẩm vào giỏ hàng khi đã có sẵn")
	public void TestAddExistingProductToCart() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();
	    
	    homePage.clickIconCart();
	    Thread.sleep(1000);
	    
	    String productName = CartPage.getProductName();
	    String option = CartPage.getOption();
	    int quantity = CartPage.getQuantityByProductName(productName);  
	    
	    CartPage.clickProduct();
	    Thread.sleep(1000);
	    
	    productDetailPage.addToCardWithOptionText(option);
	    
	    int quantityAfter = new Random().nextInt(5) + 1;  
	    productDetailPage.sendkeyInputQuantity(quantityAfter);
	    Thread.sleep(1000);
	    
	    productDetailPage.clickBtnAdd();
	    Thread.sleep(1000);
	    
	    productDetailPage.clickBtnCart();
	    Thread.sleep(1000);

	    int expectedQuantity = quantity + quantityAfter;
	    boolean result = CartPage.verifyUpdatedQuantity(productName, expectedQuantity);
	    
	    softAssert.assertTrue(result, "Lỗi số lượng sản phẩm! Mong đợi: " + expectedQuantity);
	    softAssert.assertAll();
	}

	@Test(priority = 5 , description = "TC20 - Xác minh chức năng thêm nhiều sản phẩm vào giỏ hàng và tổng tiền hiển thị")
	@Story("Thêm nhiều sản phẩm vào giỏ hàng và kiểm tra tổng giá")
	public void TestAddMultipleProductsToCart() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();
	    
	    // Chọn 2 sản phẩm khác nhau
	    String productSearch1 = "SamSung";
	    
	    int quantity1 = new Random().nextInt(5) + 1; 
	    int quantity2 = new Random().nextInt(5) + 1; 

	    // **Thêm sản phẩm 1 vào giỏ hàng**
	    searchPage.searchByNameAS(productSearch1);
	    searchPage.clickProduct(0);
	    Thread.sleep(1500);

	    productName1 = productDetailPage.getProductName();
	    productDetailPage.addToCardAS();
	    productDetailPage.sendkeyInputQuantity(quantity1);
	    productDetailPage.clickBtnAdd();
	    Thread.sleep(1000);
	    
	    // **Thêm sản phẩm 2 vào giỏ hàng**
	    homePage.clickBtnBack();
	    Thread.sleep(1000);
	    searchPage.clickProduct(1);
	    Thread.sleep(1500);

	    String productName2 = productDetailPage.getProductName();
	    productDetailPage.addToCardAS();
	    productDetailPage.sendkeyInputQuantity(quantity2);
	    productDetailPage.clickBtnAdd();
	    Thread.sleep(1000);
	    productDetailPage.clickBtnCart();


	    // **Kiểm tra tổng tiền**
	    boolean priceCheck = CartPage.verifyLatestProductPrices(productName1, productName2, quantity1, quantity2);
	    softAssert.assertTrue(priceCheck, "Tổng tiền trong giỏ hàng không chính xác!");

	    softAssert.assertAll();
	}
	
	@Test(priority = 6, description = "TC21 - Xác minh chức năng xóa 1 sản phẩm khỏi giỏ hàng")
	@Story("Xóa 1 sản phẩm khỏi giỏ hàng")
	public void TestRemoveProductFromCart() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();

	    // Mở giỏ hàng
	    homePage.clickIconCart();
	    Thread.sleep(2000);
	    boolean isProductInCart = CartPage.isProductInCartBeforeDelete(productName1);
	    softAssert.assertTrue(isProductInCart, "Sản phẩm không tồn tại trong giỏ hàng!");
	    if (isProductInCart) {
	        CartPage.swipeLeftToDelete(productName1);
	        Thread.sleep(1000);

	        boolean isProductRemoved = CartPage.isProductInCart(productName1);
	        softAssert.assertTrue(isProductRemoved, "Sản phẩm chưa được xóa khỏi giỏ hàng!");
	    
	    }
	    softAssert.assertAll();
	}
	
	@Test(priority = 7, description = "TC22 - Xác minh chức năng xóa tất cả sản phẩm khỏi giỏ hàng")
	@Story("Xóa toàn bộ sản phẩm khỏi giỏ hàng")
	public void TestClearCart() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();
       String expectedMessage = "\"Hổng\" có gì trong giỏ hết";
	    // Mở giỏ hàng
	    homePage.clickIconCart();
	    Thread.sleep(2000);

	    // Kiểm tra giỏ hàng có sản phẩm không
	    boolean isCartNotEmpty = !CartPage.productNames.isEmpty();
	    softAssert.assertTrue(isCartNotEmpty, "Giỏ hàng đã trống, không có sản phẩm để xóa!");
	    
	    if (isCartNotEmpty) {
	        // Gọi hàm xóa toàn bộ sản phẩm
	        CartPage.clearCart(driver);
	        Thread.sleep(1000);
	        boolean isCartCleared = CartPage.verifyCartEmptyMessage(expectedMessage);
	        // Kiểm tra giỏ hàng đã trống chưa
	        softAssert.assertTrue(isCartCleared, "Giỏ hàng chưa được xóa hết!");
	    }

	    softAssert.assertAll();
	}


	@Test(priority = 8, description = "TC23 - Xác minh chức năng thêm sản phẩm vào giỏ hàng từ danh sách yêu thích")
	@Story("Thêm sản phẩm vào giỏ hàng từ danh sách yêu thích")
	public void TestAddProductFromFavoriteToCart() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();

	    // Truy cập danh sách yêu thích
	    homePage.clickFavoriteAS();
	    Thread.sleep(1000);

	    String productName = "Máy sấy tóc KANING sấy 2 chiều 3 chế độ nhiệt tạo kiểu dễ dàng tặng kèm bộ 1 - 3 hoặc 5 phụ kiện tùy phân loại máy sấy";
	    
	    // Click vào nút "Thêm vào giỏ hàng" tương ứng
	    favoritePage.clickAddToCartByProductName(productName);
	    Thread.sleep(1500);
	    
	    productDetailPage.chooseOption();

	    // Chọn số lượng ngẫu nhiên từ 1 - 5
	    int quantity = new Random().nextInt(5) + 1;
	    productDetailPage.sendkeyInputQuantity(quantity);
	    Thread.sleep(1500);

	    // Lấy option đã chọn nếu có
	    String option = productDetailPage.getOptionSelected();
	    System.out.println("Tên: " + productName + ", Option: " + option);

	    // Click vào nút "Thêm vào giỏ hàng"
	    favoritePage.clickBtnAddToCard();
	    Thread.sleep(1000);

	    // Kiểm tra thông báo toast hiển thị đúng
	    boolean toastResult = productDetailPage.checkToastMessage("Đã thêm vào giỏ");
	    softAssert.assertTrue(toastResult, "Thông báo 'Đã thêm vào giỏ' không hiển thị!");

	    // Truy cập vào giỏ hàng
	    favoritePage.clickGoToCard();
	    
	    // Kiểm tra sản phẩm có trong giỏ hàng không
	    boolean cartResult = CartPage.verifyProductDetails(productName, option, quantity);
	    softAssert.assertTrue(cartResult, "Thông tin sản phẩm trong giỏ hàng không khớp!");

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
