package TestCase;

import java.net.MalformedURLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Base.Basic;
import Page.AddProductPage;
import Page.HomePage;
import Page.LoginPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import utils.listeners.AllureTestListener;

@Feature("Kiểm tra chức năng tạo sản phẩm")
public class TestCaseAddProduct extends Basic{
	HomePage homePage;
	AddProductPage addProductPage;
	LoginPage loginPage;
	SoftAssert softAssert;
	
	@BeforeMethod
    public void SetUp() throws MalformedURLException {
        
        configureAppium(); // Khởi tạo lại driver
        
        homePage = new HomePage(driver);
        addProductPage = new AddProductPage(driver);
        loginPage = new LoginPage(driver);
        
        
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        ITestContext context = Reporter.getCurrentTestResult().getTestContext();
        context.setAttribute("driver", driver);
        
        homePage.clickBtnStart();
        homePage.clickLoginAS();
        
        loginPage.loginA("0333085521", "Quang0511@");
        try {
            Thread.sleep(3000);
            homePage.clickBtnBack();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Qua rồi");
        homePage.clickAddNewProductAS();
        addProductPage.clickBtnClose();
    }
	
	
	@Test(priority = 1, description = "TC07 - Kiểm tra ảnh hiển thị sau khi chụp")
	@Story("Kiểm tra ảnh hiển thị ")
	public void testCheckCapturedImage() throws InterruptedException {
	    softAssert = new SoftAssert();
        // Chụp ảnh sản phẩm
        addProductPage.takePhotoAS();
        Allure.step("Đã chụp ảnh sản phẩm");
        Thread.sleep(1000);
	    Allure.step("Kiểm tra ảnh hiển thị", () -> {
	    	
	        boolean isImageMatched = addProductPage.isImageDisplayedAfterUse(driver);
	        softAssert.assertTrue(isImageMatched, "Kiểm tra ảnh hiển thị sau khi chụp thất bại!");

	    });
	    
	    softAssert.assertAll();
	}
	

	
	
	@Test(priority = 2, description = "TC08 - Kiểm tra chức năng thêm sản phẩm với tên sản phẩm < 10 kí tự và mô tả < 100 kí tự")
	@Story("Kiểm tra nhập tên sản phẩm và lỗi hiển thị")
	@Step("Nhập tên sản phẩm với độ dài ngẫu nhiên từ 1 đến 9 ký tự , mô tả từ 1 đến 99 kí tự và kiểm tra thông báo lỗi")
	public void testCheckErrorProductNameAndDescription() throws InterruptedException {
	    softAssert = new SoftAssert();
	    
	    // Tạo tên sản phẩm ngẫu nhiên từ 1 đến 9 ký tự
	    int nameLength = new Random().nextInt(9) + 1;
	    String shortProductName = RandomStringUtils.randomAlphabetic(nameLength);
	    Allure.step("Tên sản phẩm được tạo: " + shortProductName);

	    // Nhập tên sản phẩm vào form
	    addProductPage.sendKeyProductName(shortProductName);

	    // Xác định số ký tự thiếu
	    int missingChars = 10 - nameLength;
	    String expectedErrorMessageName = "Còn thiếu " + missingChars + " ký tự";

	    String shortDescription = RandomStringUtils.randomAlphabetic(new Random().nextInt(99) + 1);
	    addProductPage.sendKeyProductDescription(shortDescription);
	    int missingDescChars = 100 - shortDescription.length();
	    String expectedErrorMessageDesc = "Còn thiếu " + missingDescChars + " ký tự";

	    // Kiểm tra thông báo lỗi của cả tên và mô tả sản phẩm
	    Allure.step("Bước kiểm tra nhập tên và mô tả sản phẩm", () -> {  
	        boolean isErrorDisplayed = addProductPage.verifyProductNameAndDescriptionErrorMessage(
	            expectedErrorMessageName, expectedErrorMessageDesc
	        );
	        softAssert.assertTrue(isErrorDisplayed, "Thông báo lỗi không hiển thị hoặc không đúng!");
	    });

	    softAssert.assertAll();
	}
	
	
	@Test(priority = 3, description = "TC09 - Kiểm tra chức năng thêm sản phẩm với các trường ảnh, tên, mô tả, ngành hàng và giá để trống")
	@Story("Kiểm tra chức năng thêm sản phẩm với các trường thông tin trống và lỗi hiển thị")
	public void testCheckProductFieldsEmpty() throws InterruptedException {
	    SoftAssert softAssert = new SoftAssert();

	    Allure.step("Nhấn vào nút hiển thị sản phẩm");
	    addProductPage.clickBtnDisPlay();  

	    // Kiểm tra thông báo lỗi của tất cả các trường
	    Allure.step("Kiểm tra thông báo lỗi khi để trống các trường sản phẩm", () -> {  
	        boolean isErrorDisplayed = addProductPage.verifyAllErrorMessages(
	            "Vui lòng đăng tải tối thiểu 1 hình ảnh về sản phẩm này.", 
	            "Vui lòng nhập Tên sản phẩm", 
	            "Vui lòng nhập Mô tả sản phẩm", 
	            "Vui lòng chọn danh mục ngành hàng", 
	            "Vui lòng nhập Giá"
	        );
	        softAssert.assertTrue(isErrorDisplayed, "Thông báo lỗi không hiển thị hoặc không đúng!");
	    });

	    softAssert.assertAll();
	}
	
	@Test(priority = 4, description  = "TC10 - Kiểm tra hiển thị Toast khi chọn ảnh quá số lượng ảnh")
	@Story("Kiểm tra chọn ảnh từ thư viện với số lượng vượt mức cho phép")
    public void testToastMessageForInvalidImage() throws InterruptedException {
		softAssert = new SoftAssert();
		addProductPage.chooseImageAS();
        addProductPage.checkCheckboxes(10);
        Thread.sleep(1000);
        boolean isToastCorrect = addProductPage.checkToastMessage("Bạn đã vượt quá giới hạn tối đa");

        Assert.assertTrue(isToastCorrect, "Toast không hiển thị đúng nội dung mong đợi!");
        softAssert.assertAll();
    }
	
	@Test(priority = 5, description = "TC11 - Kiểm tra lỗi khi nhập cân nặng bằng 0")
	@Story("Kiểm tra lỗi khi nhập cân nặng không hợp lệ (0kg)")
	public void testAddProductWithInvalidWeightZero() throws InterruptedException {
	    softAssert = new SoftAssert();
	    
	    // Khai báo dữ liệu đầu vào
	    String productName = "Áo len thời trang";
	    String productDescription = "Áo len thời trang mang đến sự ấm áp và phong cách trong mùa lạnh."
	            + " Với chất liệu mềm mại, co giãn tốt, áo len không chỉ giữ ấm mà còn tạo sự thoải mái khi mặc. "
	            + "Thiết kế đa dạng từ dáng ôm, dáng rộng đến áo len cổ lọ hay cardigan giúp bạn dễ dàng phối đồ "
	            + "theo nhiều phong cách khác nhau";
	    String productPrice = "2800000";
	    String productStock = "10";
	    String productWeight = "0"; // Cân nặng không hợp lệ (phải > 0)
	    String category = "Thời Trang Nam";
	    String subCategory = "Áo len";
	    String brandName = "ADAM STORE";

	    // Chụp ảnh sản phẩm
	    addProductPage.takePhotoAS();

	    // Thêm sản phẩm với cân nặng không hợp lệ
	    addProductPage.addProductAS(productName, productDescription, productPrice, productStock, productWeight, category, subCategory, brandName);
	    Thread.sleep(500);

	    String expectedErrorMessage = "Cân nặng phải lớn hơn 0";
	    boolean isErrorDisplayed = addProductPage.verifyWeightErrorMessage(expectedErrorMessage);

	    Assert.assertTrue(isErrorDisplayed, "❌ Thông báo lỗi không hiển thị hoặc không đúng khi nhập cân nặng = 0!");

	    softAssert.assertAll();
	}

	
	@Test(priority = 6, description = "TC12 - Kiểm tra chức năng thêm sản phẩm hiển thị với ảnh được chọn từ thư viện")
	@Story("Kiểm tra chức năng thêm sản phẩm hiển thị với ảnh chọn từ thư viện")
	public void testAddNewProductWithImageFromLibrary() throws InterruptedException {
	    softAssert = new SoftAssert();
	    
	    // Khai báo dữ liệu đầu vào
	    String productName = "Điện thoại Oppo";
	    String productDescription = "OPPO Reno 13 đã chính thức gia nhập thị trường điện thoại thông minh. "
	            + "Với chất liệu bền bỉ, khả năng kháng nước và bụi tốt, "
	            + "cùng vi xử lý chuẩn flagship và viên pin dung lượng lớn, "
	            + "hứa hẹn mang đến trải nghiệm tuyệt vời cho người dùng.";
	    String productPrice = "18000000";
	    String productStock = "10";
	    String productWeight = "120";
	    int imageCount = 2;
	    String category = "Điện Thoại & Phụ Kiện";
	    String subCategory = "Điện thoại";
	    String brandName = "No brand";
	    addProductPage.chooseImageAS();
	    addProductPage.checkCheckboxes(imageCount);
	    addProductPage.clickBtnAction();
	    // Thực hiện thêm sản phẩm
	    addProductPage.addProductAS(productName, productDescription, productPrice, productStock, productWeight, category, subCategory, brandName);
	    addProductPage.clickBtnSave();
		addProductPage.clickBtnDisPlay();
		
    addProductPage.checkToastMessage("Hiển thị thành công");

    // Vuốt màn hình để cập nhật danh sách sản phẩm
    addProductPage.swipeToExactPosition(582, 806, 1644, 1500);
	    // Kiểm tra sản phẩm có được thêm thành công không
	    boolean isProductDisplayed = addProductPage.verifyAddNewProduct(productName);
	    
	    // Kiểm tra và báo lỗi nếu sản phẩm không hiển thị sau khi thêm
	    Assert.assertTrue(isProductDisplayed, "❌ Sản phẩm '" + productName + "' không hiển thị sau khi thêm!");
	    
	    softAssert.assertAll();
	}
	
	
	@Test(priority = 7, description = "TC13 - Kiểm tra thêm sản phẩm hiển thị với ảnh chụp & tồn kho = 0")
	@Story("Thêm sản phẩm hiển thị với ảnh chụp từ thư viện & số lượng tồn kho bằng 0")
	public void testAddProductWithCapturedImageAndZeroStock() throws InterruptedException {
	    softAssert = new SoftAssert();
	    
	    // Khai báo dữ liệu đầu vào
	    String productName = "Áo len thời trang";
	    String productDescription = "Áo len thời trang mang đến sự ấm áp và phong cách trong mùa lạnh."
	    		+ " Với chất liệu mềm mại, co giãn tốt, áo len không chỉ giữ ấm mà còn tạo sự thoải mái khi mặc. "
	    		+ "Thiết kế đa dạng từ dáng ôm, dáng rộng đến áo len cổ lọ hay cardigan giúp bạn dễ dàng phối đồ "
	    		+ "theo nhiều phong cách khác nhau";
	    String productPrice = "2800000";
	    String productStock = "0";
	    String productWeight = "120";
	    String category = "Thời Trang Nam";
	    String subCategory = "Áo len";
	    String brandName = "ADAM STORE";
	    // Chụp ảnh sản phẩm
	    addProductPage.takePhotoAS();

	    // Thêm sản phẩm
	    addProductPage.addProductAS(productName, productDescription, productPrice, productStock, productWeight, category, subCategory,brandName);
	    addProductPage.clickBtnSave();
	    addProductPage.clickBtnDisPlay();
	    Thread.sleep(500);
	    
	    addProductPage.checkToastMessage("Hiển thị thành công");
	    addProductPage.chooseOption("Hết hàng");
	    // Vuốt màn hình để cập nhật danh sách sản phẩm
	    addProductPage.swipeToExactPosition(582, 806, 1644, 1500);
	    
	    // Kiểm tra sản phẩm có hiển thị không
	    boolean isProductDisplayed = addProductPage.verifyAddNewProduct(productName);
	    
	    // Kiểm tra kết quả & báo lỗi nếu cần
	    Assert.assertTrue(isProductDisplayed, "❌ Sản phẩm '" + productName + "' không hiển thị sau khi thêm!");
	    
	    softAssert.assertAll();
	}
	
	@Test(priority = 8, description = "TC14 - Kiểm tra chức năng lưu sản phẩm")
	@Story("Kiểm tra chức năng lưu sản phẩm")
	public void testSaveProductSuccessfully () throws InterruptedException {
	    softAssert = new SoftAssert();
	    
	    // Khai báo dữ liệu đầu vào
	    String productName = "Áo len thời trang";
	    String productDescription = "Áo len thời trang mang đến sự ấm áp và phong cách trong mùa lạnh."
	    		+ " Với chất liệu mềm mại, co giãn tốt, áo len không chỉ giữ ấm mà còn tạo sự thoải mái khi mặc. "
	    		+ "Thiết kế đa dạng từ dáng ôm, dáng rộng đến áo len cổ lọ hay cardigan giúp bạn dễ dàng phối đồ "
	    		+ "theo nhiều phong cách khác nhau";
	    String productPrice = "2800000";
	    String productStock = "10";
	    String productWeight = "120";
	    String category = "Thời Trang Nam";
	    String subCategory = "Áo len";
	    String brandName = "ADAM STORE";
	    // Chụp ảnh sản phẩm
	    addProductPage.takePhotoAS();

	    // Thêm sản phẩm
	    addProductPage.addProductAS(productName, productDescription, productPrice, productStock, productWeight, category, subCategory,brandName);
	    addProductPage.clickBtnSave();
	    addProductPage.clickBtnSave();
	    Thread.sleep(1000);
	    
	    addProductPage.checkToastMessage("Sửa sản phẩm thành công");
	    addProductPage.swipeHorizontally(861, 165, 266, 1500);
	    addProductPage.swipeHorizontally(861, 165, 600, 1500);
	    
	    Thread.sleep(2000);
	    addProductPage.chooseOption("Đã ẩn");
	    addProductPage.chooseOption("Đã ẩn");
	    // Vuốt màn hình để cập nhật danh sách sản phẩm
	    
	    addProductPage.swipeToExactPosition(582, 806, 1644, 1500);
	    
	    Thread.sleep(1000);
	    // Kiểm tra sản phẩm có hiển thị không
	    boolean isProductDisplayed = addProductPage.verifyAddNewProduct(productName);
	    
	    // Kiểm tra kết quả & báo lỗi nếu cần
	    Assert.assertTrue(isProductDisplayed, "Sản phẩm '" + productName + "' không hiển thị sau khi thêm!");
	    
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
