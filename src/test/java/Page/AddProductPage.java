package Page;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.analysis.function.Add;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Supplier;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.listeners.Utils;

public class AddProductPage {
	private AndroidDriver driver;
	private WebDriverWait wait;
	private Actions ac;
	private Utils utils;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Thêm ảnh\")")
	private WebElement btnAddImage;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Chụp ảnh\")")
	private WebElement optionTakeAPhoto;
	
	@AndroidFindBy(id = "com.shopee.vn:id/capture_camera")
	private WebElement btnCaptureImage ;
	
	@AndroidFindBy(id="com.shopee.vn:id/use_photo")
	private WebElement btnUseImage;
	
	@AndroidFindBy(id = "com.shopee.vn:id/image_preview")
	private WebElement imageReview;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.ImageView")
	private WebElement imageDisplay;
	
	@AndroidFindBy(uiAutomator  = "new UiSelector().text(\"Nhập tên sản phẩm\")")
	private WebElement inputProductName;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Nhập mô tả sản phẩm\")")
	private WebElement inputProductDescription;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Ngành hàng\")")
	private WebElement selectBranch;
	
	@AndroidFindBy(xpath  = "//android.widget.TextView[@text=\"Điện Thoại & Phụ Kiện\"]")
	private WebElement optionPhoneAndAccessory;
	
	@AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Điện thoại\"]")
	private WebElement optionPhone;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Thương hiệu\")")
	private WebElement selecTrademark;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"No brand\")")
	private WebElement optionNoBrand;
	
	@AndroidFindBy(xpath = "(//android.view.ViewGroup[@resource-id=\"product_name_input_box\"])[2]/android.view.ViewGroup")
	private WebElement boxDescription;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[3]//android.widget.TextView[@index='0']")
	private WebElement spanErrorProductImage;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]//android.widget.TextView[@index='0']")
	private WebElement spanErrorProductName;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[2]//android.widget.TextView[@index='0']")
	private WebElement spanErrorProductDescription;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[2]//android.widget.TextView[@index='0']")
	private WebElement errorProductName;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup[2]//android.widget.TextView[@index='0']")
	private WebElement errorProductDescription;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup[2]//android.widget.TextView[@index='0']")
	private WebElement spanErrorProductBranch;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Thư viện hình ảnh\")")
	private WebElement optionImageLib ;
	
    @AndroidFindBy(xpath = "(//android.widget.CheckBox[@resource-id=\"com.shopee.vn:id/checkbox\"])")
    private List<WebElement> checkboxList;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Đặt\")")
    private WebElement inputPrice;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"0\")")
    private WebElement inputInventory;
    
   @AndroidFindBy(xpath = "//androidx.viewpager.widget.ViewPager/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[3]")
   private WebElement optionInventory;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Phí vận chuyển\")")
    private WebElement shippingFee;
    
    @AndroidFindBy(uiAutomator  = "new UiSelector().text(\"Nhập cân nặng\")")
    private WebElement inputWeight;
    
    @AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]//android.widget.TextView[@index='0']")
    private WebElement spanErrorWeight;
    
    @AndroidFindBy(uiAutomator  = "new UiSelector().text(\"Lưu\")")
	private WebElement btnSave;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Hiển thị\")")
    private WebElement btnDisplay;
    
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Cập nhật\"]")
    private WebElement btnUpdate;
    
	@AndroidFindBy(className = "android.widget.Toast")
	private WebElement toastErrorImage;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[contains(@resource-id, '_product_item_cell')]/android.view.ViewGroup[2]//android.view.ViewGroup[contains(@resource-id, '_product_stock')]/android.widget.TextView[@index='1']")
	List<WebElement> listInventory;
	
	@AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Cuối trang\"]")
	WebElement lastPage;
//	@AndroidFindBy(xpath = "new UiSelector().text(\"Khoảng giới hạn cho Giá = 1000.00 ~ 120000000.00\")")
//	private WebElement spanErrorProductPrice;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[2]//android.widget.TextView[@index='0']")
	private WebElement spanErrorProductPrices;
	
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.ImageView")
	private WebElement btnClose;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[2]//android.widget.TextView[@index='0']")
	private WebElement productIndustry;
	

	@AndroidFindBy(xpath = "(//android.view.ViewGroup[contains(@resource-id, \"_product_name_and_price_label\")])[1]//android.widget.TextView[@index='0']")
	private WebElement productDisplay;

	
	@AndroidFindBy(id = "com.shopee.vn:id/action")
	private WebElement btnAction;
	
	@AndroidFindBy(id = "com.shopee.vn:id/buttonDefaultPositive")
	private WebElement btnConfirm;
	
	@AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Giá và tồn kho\"]")
	private WebElement btnPriceAndInventory;
	
	@AndroidFindBy(xpath = "(//android.view.ViewGroup[@resource-id=\"price_input_box\"])[1]//android.widget.EditText")
	private WebElement inputPriceEdit;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"stock_edit_cell\"]/android.view.ViewGroup/android.view.ViewGroup[2]//android.widget.EditText")
	private WebElement inputInventoryEdit;	
    public AddProductPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ac = new Actions(driver);
        utils = new Utils(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    
    private AddProductPage clickBtnAddImage() {
    	Allure.step("Nhấn vào mục thêm ảnh");
    	wait.until(ExpectedConditions.elementToBeClickable(btnAddImage)).click();
    	return this;
    }
    
    private AddProductPage clickOptionTakePhoto() {
    	Allure.step("Nhấn chọn option Chụp ảnh");
    	wait.until(ExpectedConditions.elementToBeClickable(optionTakeAPhoto)).click();
    	return this;
    }
    
    private AddProductPage clickBtnCaptureImage() {
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Allure.step("Nhấn nút chụp ảnh");
    	wait.until(ExpectedConditions.elementToBeClickable(btnCaptureImage)).click();
    	return this;
    }
    
    private AddProductPage clickBtnUseImage() {
    	Allure.step("Nhấn nút sử dụng ảnh");
    	wait.until(ExpectedConditions.elementToBeClickable(btnUseImage)).click();
    	return this;
    }
    
    public AddProductPage sendKeyProductName(String productName) {
    	Allure.step("Nhập tên sản phẩm: "+ productName);
    	wait.until(ExpectedConditions.visibilityOf(inputProductName)).sendKeys(productName);
    	return this;
    }
    
    public AddProductPage sendKeyProductDescription(String description) throws InterruptedException {
        Allure.step("Nhập mô tả: " + description);
        boxDescription.click();

        driver.findElement(AppiumBy.androidUIAutomator(
        	    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"Nhập mô tả sản phẩm\"))"
        	));

        wait.until(ExpectedConditions.visibilityOf(inputProductDescription)).sendKeys(description);

        if (((AndroidDriver) driver).isKeyboardShown()) {
            System.out.println("Bàn phím đang mở, sẽ ẩn đi...");
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK)); 
        }

        Thread.sleep(1000);
        return this;
    }

    

    
    public AddProductPage clickBtnClose() {
    	wait.until(ExpectedConditions.elementToBeClickable(btnClose)).click();
    	return this;
    }
    
    public AddProductPage clickBtnDisPlay() {
    	Allure.step("Nhấn nút hiển thị");
    	wait.until(ExpectedConditions.elementToBeClickable(btnDisplay)).click();
    	return this;
    }
    
    
    public AddProductPage chooseBrand() {
    	Allure.step("Nhấn chọn ngành hàng");
    	wait.until(ExpectedConditions.visibilityOf(selectBranch));
    	 selectBranch.click();
    	return this;
    }
    
    public AddProductPage chooseOption(String categoryName) {
        String dynamicXPath = String.format("//android.widget.TextView[@text=\"%s\"]", categoryName);
        WebElement categoryOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dynamicXPath)));
        
        Allure.step("Nhấn chọn option: " + categoryName);
        categoryOption.click();
        
        return this;
    }

    
    
    public AddProductPage choosePhone() {
    	Allure.step("Nhấn chọn option Điện thoại");
    	wait.until(ExpectedConditions.elementToBeClickable(optionPhone)).click();
    	return this;
    }
    
    
    public AddProductPage chooseBrandAS(String caterory, String subCategory) {
        return Allure.step("Thực hiện chọn ngành hàng", () -> {
            chooseBrand();
            chooseOption(caterory);
            chooseOption(subCategory);
            return this; 
        });
    }
    
   public AddProductPage chooseTrademark() {
	   Allure.step("Nhấn chọn Thương hiệu");
	   	wait.until(ExpectedConditions.elementToBeClickable(selecTrademark)).click();
	   	return this;
   }
   
   public AddProductPage clickSortInventoryDescending() {
	    Allure.step("Nhấn chọn sắp xếp theo tồn kho giảm dần");
	    wait.until(ExpectedConditions.elementToBeClickable(optionInventory)).click();
	    return this;
	}

	public AddProductPage clickSortInventoryAscending() throws InterruptedException {
	    Allure.step("Nhấn chọn sắp xếp theo tồn kho tăng dần");
	    wait.until(ExpectedConditions.elementToBeClickable(optionInventory)).click();
	    Thread.sleep(2000); 
	    wait.until(ExpectedConditions.elementToBeClickable(optionInventory)).click();
	    return this;
	}

   
//   public AddProductPage chooseOptionNoBrand(String trademark) {
//	   String dynamicXPath = String.format("//android.widget.TextView[@text=\"%s\"]", categoryName);
//       WebElement categoryOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dynamicXPath)));
//       
//       Allure.step("Nhấn chọn option: " + categoryName);
//       categoryOption.click();
//       
//       return this;
//   }
   
   public AddProductPage chooseTrademarkAS(String brandName) {
       return Allure.step("Thực hiện chọn thương hiệu", () -> {
    	   chooseTrademark();
    	   chooseOption(brandName);
    	   clickBtnSave();
           return this; 
       });
   }
   
   public AddProductPage clickBtnAction() {
   	Allure.step("Nhấn nút xác nhận");
   	wait.until(ExpectedConditions.elementToBeClickable(btnAction)).click();
   	return this;
   }
   
   public AddProductPage sendkeyInputPrice(String price) {
	    Allure.step("Nhập giá: " + price);
	    wait.until(ExpectedConditions.visibilityOf(inputPrice));
	    inputPrice.sendKeys(price);
	    return this;
	}
   
   public AddProductPage sendkeyInputInventory(String quantity) {
	   Allure.step("Nhập số lượng tồn kho : " + quantity);
	   wait.until(ExpectedConditions.visibilityOf(inputInventory));
	   inputInventory.click();
	   ac.sendKeys(quantity).perform();
	   if (((AndroidDriver) driver).isKeyboardShown()) {
           ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK)); 
       }
	   return this;
   }
   
   
   public AddProductPage chooseShippingFee() {
  	 Allure.step("Nhấn chọn phí vận chuyển");
	   	wait.until(ExpectedConditions.elementToBeClickable(shippingFee)).click();
	   	return this;
  }
  
  public AddProductPage sendKeyInputWeight(String weight) {
 	 Allure.step("Nhập cân nặng sản phẩm: " + weight);
	   	wait.until(ExpectedConditions.visibilityOf(inputWeight));
	   	inputWeight.sendKeys(weight);
	   	return this;
 }
  
  public AddProductPage clickBtnSave() {
	  Allure.step("Nhấn nút Lưu");
	   	wait.until(ExpectedConditions.elementToBeClickable(btnSave)).click();
	   	return this;
  }
   
  public AddProductPage shippingFeeAS(String weight) {
      return Allure.step("Thực hiện nhập phí vận chuyển", () -> {
    	  	chooseShippingFee();
   	   		sendKeyInputWeight(weight);
   	   		
          return this; 
      });
  }
  
  
    public AddProductPage takePhotoAS() {
    	clickBtnAddImage();
    	clickOptionTakePhoto();
    	clickBtnCaptureImage();
    	clickBtnUseImage();
    	return this;
    }
    



    
    public AddProductPage chooseImageAS() {
    	clickBtnAddImage();
    	clickOptionImageLib();
    	return this;
    }
    

    
    public void checkCheckboxes(int quantity) {
    	Allure.step("Chọn " +quantity+ " ảnh từ thư viện");
        int count = Math.min(quantity, checkboxList.size());
     
        
        for (int i = 0; i < count; i++) {
            if (!checkboxList.get(i).isSelected()) { 
                checkboxList.get(i).click();
            }
        }
    }
    
    private AddProductPage clickOptionImageLib() {
    	Allure.step("Nhấn chọn option chọn ảnh từ thư viện");
    	wait.until(ExpectedConditions.elementToBeClickable(optionImageLib)).click();
    	return this;
    }
    
    private AddProductPage clickBtnConfirm() {
    	Allure.step("Nhấn nút xác nhận");
    	wait.until(ExpectedConditions.elementToBeClickable(btnConfirm)).click();
    	return this;
    }
    
    public AddProductPage clickBtnUpdate() {
    	Allure.step("Nhấn nút cập nhật");
    	wait.until(ExpectedConditions.elementToBeClickable(btnUpdate)).click();
    	return this;
    }
    
    public AddProductPage clickBtnPriceAndInventory() {
    	Allure.step("Nhấn nút giá và tồn kho");
    	btnPriceAndInventory.click();
    	return this;
    }
    
    public AddProductPage sendKeyPrice(String price) {
    	
    	wait.until(ExpectedConditions.visibilityOf(inputPriceEdit));
    	Allure.step("Xóa dữ liệu cũ trong ô input giá");
    	inputPriceEdit.clear();
    	Allure.step("Nhập giá vào ô input: " + price);
    	inputPriceEdit.sendKeys(price);
    	return this;
    }
    
    public AddProductPage sendKeyInventory(String inventory) {
    	
    	wait.until(ExpectedConditions.visibilityOf(inputInventoryEdit));
    	Allure.step("Xóa dữ liệu cũ trong ô input tồn kho");
    	inputInventoryEdit.clear();
    	Allure.step("Nhập giá vào ô input: " + inventory);
    	inputInventoryEdit.sendKeys(inventory);
    	return this;
    }
    
    public AddProductPage addProductAS(String productName, String description, String price, String inventory, String weight, String category, String subCategory, String brandName) throws InterruptedException {
    	
    	Thread.sleep(1000);
    	
    	
    	sendKeyProductName(productName);
    	sendKeyProductDescription(description);
    	swipeToExactPosition(534, 2030, 1000, 1500);
    	chooseBrandAS(category,subCategory);
    	Thread.sleep(1000);
    	chooseTrademarkAS(brandName);
    	Thread.sleep(1000);
    	swipeToExactPosition(534, 2030, 1000, 1500);
    	sendkeyInputPrice(price);
    	sendkeyInputInventory(inventory);
    	shippingFeeAS(weight);
    	
    	return this;
    }
    
    public AddProductPage clickBtnHiddenByProductName(String name) {
        Allure.step("Tìm và nhấn nút Ẩn cho sản phẩm: " + name, () -> {
            boolean found = false;
            
            while (!found) {
                List<WebElement> productCards = driver.findElements(By.xpath("//android.view.ViewGroup[contains(@resource-id, '_product_item_cell')]"));
                System.out.println("Số lượng productCards: "+ productCards);
                for (WebElement card : productCards) {
                    try {
                        WebElement nameElement = card.findElement(By.xpath(".//android.view.ViewGroup[1]//android.view.ViewGroup//android.view.ViewGroup[contains(@resource-id, '_product_name_and_price_label')]//android.widget.TextView[1]"));
                        System.out.println("Số lượng tên: "+ nameElement);
                        if (nameElement.getText().trim().equals(name)) {
                            WebElement hideButton = card.findElement(By.xpath(".//android.widget.TextView[@text='Ẩn']"));
                            hideButton.click();
                            found = true;

                            Allure.step("Nhấn nút ẩn sản phẩm: " + name);
                            Thread.sleep(1000);
                            clickBtnConfirm();
                            break;
                        }
                    } catch (Exception e) {
                    }
                }

                if (found || isElementVisible(lastPage)) break;

                utils.scrollDown();
                Thread.sleep(500);
            }

            if (!found) {
                Allure.step("Không tìm thấy sản phẩm: " + name);
                throw new RuntimeException("Không tìm thấy sản phẩm có tên: " + name);
            }
        });

        return this;
    }
    
    public AddProductPage clickBtnSeeMore(String name) {
        Allure.step("Tìm và nhấn nút Ẩn cho sản phẩm: " + name, () -> {
            boolean found = false;
            
            while (!found) {
                List<WebElement> productCards = driver.findElements(By.xpath("//android.view.ViewGroup[contains(@resource-id, '_product_item_cell')]"));

                for (WebElement card : productCards) {
                    try {
                        WebElement nameElement = card.findElement(By.xpath(".//android.view.ViewGroup[1]//android.view.ViewGroup//android.view.ViewGroup[contains(@resource-id, '_product_name_and_price_label')]//android.widget.TextView[1]"));
                        System.out.println("Tên: " + nameElement.getText());
                        if (nameElement.getText().trim().equals(name)) {
                        	WebElement seeMoreButton = card.findElement(By.xpath(".//android.view.ViewGroup[@resource-id='see_more_button']"));
                            seeMoreButton.click();
                            found = true;
                            
                            Allure.step("Nhấn nút ẩn xem thêm ");
                            Thread.sleep(1000);
                            clickBtnPriceAndInventory();
                            break;
                        }
                    } catch (Exception e) {
                    }
                }

                if (found || isElementVisible(lastPage)) break;

                utils.scrollDown();
                Thread.sleep(500);
            }

            if (!found) {
                Allure.step("Không tìm thấy sản phẩm: " + name);
                throw new RuntimeException("Không tìm thấy sản phẩm có tên: " + name);
            }
        });

        return this;
    }
    
    public boolean verifyAddNewProduct(String productName) throws InterruptedException {
    	
    	Thread.sleep(2000);
    	System.out.println("Tên sản phẩm: " + productDisplay.getText());
        return Allure.step("Kiểm tra sản phẩm hiển thị sau khi thêm: " + productName, () -> {    
            try {
                wait.until(ExpectedConditions.visibilityOf(productDisplay));
                String displayedText = productDisplay.getText(); // Lấy text của phần tử
                
                if (displayedText.equals(productName)) {
                    Allure.step("Sản phẩm '" + productName + "' đã được thêm thành công.");
                    return true;
                } else {
                    Allure.step("Sản phẩm hiển thị: '" + displayedText + "', nhưng mong đợi: '" + productName + "'");
                    return false;
                }
            } catch (Exception e) {
                Allure.step("Sản phẩm '" + productName + "' không hiển thị sau khi thêm.");
                return false;
            }
        });
    }
    
    
    public boolean isImageDisplayedAfterUse(AndroidDriver driver) {
        return Allure.step("Kiểm tra ảnh hiển thị sau khi chụp", () -> {
            try {
                
                boolean isDisplayed = wait.until(ExpectedConditions.visibilityOf(imageDisplay)).isDisplayed();
                if (!isDisplayed) {
                    throw new AssertionError("❌ LỖI: Ảnh không hiển thị sau khi chụp!");
                }

    	        if (isDisplayed) {
    	            Allure.step("Ảnh đã hiển thị sau khi chụp!");
    	            return true;
    	        } else {
    	            Allure.step("Ảnh chụp không hiển thị!");
    	            return false;
    	        }
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Step("Kiểm tra thông báo lỗi nhập tên và mô tả sản phẩm")
    public boolean verifyProductNameAndDescriptionErrorMessage(String expectedMessageName, String expectedMessageDescription) {
        boolean isNameCorrect = isErrorMessageCorrect(errorProductName, expectedMessageName, "Tên sản phẩm");
        boolean isDescriptionCorrect = isErrorMessageCorrect(errorProductDescription, expectedMessageDescription, "Mô tả sản phẩm");

        return isNameCorrect && isDescriptionCorrect;
    }
    
    @Step("Kiểm tra thông báo lỗi của trường giá")
    public boolean verifyWeightErrorMessage(String expectedMessage) {
    	boolean isWeightCorrect = isErrorMessageCorrect(spanErrorWeight, expectedMessage, "Cân nặng");
        return isWeightCorrect;
    }

    private boolean isErrorMessageCorrect(WebElement element, String expectedMessage, String fieldName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            String actualMessage = element.getText();
            System.out.println("Thông báo : " + actualMessage);
            if (actualMessage.equals(expectedMessage)) {
                Allure.step("Thông báo lỗi cho " + fieldName + " hiển thị đúng: " + actualMessage);
                return true;
            } else {
                Allure.step("Thông báo lỗi sai cho " + fieldName + "! Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Allure.step("Không tìm thấy thông báo lỗi cho " + fieldName + " hoặc xảy ra lỗi trong quá trình kiểm tra.");
            return false;
        }
    }
    
    @Step("Kiểm tra tất cả thông báo lỗi trống")
    public boolean verifyAllErrorMessages(
            String expectedMessageImage, 
            String expectedMessageName, 
            String expectedMessageDescription, 
            String expectedMessageBranch, 
            String expectedMessagePrice) throws InterruptedException {
        
        // Lấy lỗi của 3 trường đầu tiên
        boolean isImageCorrect = isErrorMessageCorrect(spanErrorProductImage, expectedMessageImage, "Ảnh sản phẩm");
        boolean isNameCorrect = isErrorMessageCorrect(spanErrorProductName, expectedMessageName, "Tên sản phẩm");
        boolean isDescriptionCorrect = isErrorMessageCorrect(spanErrorProductDescription, expectedMessageDescription, "Mô tả sản phẩm");
        boolean isBranchCorrect = isErrorMessageCorrect(spanErrorProductBranch, expectedMessageBranch, "Danh mục ngành hàng");
        // Vuốt màn hình xuống để thấy các lỗi còn lại
        Allure.step("Cuộn xuống để kiểm tra lỗi ngành hàng và giá");
        swipeToExactPosition(582, 2030, 563, 1500);
        Thread.sleep(1000);

        // Lấy lỗi của ngành hàng và giá
        
        boolean isPriceCorrect = isErrorMessageCorrect(spanErrorProductPrices, expectedMessagePrice, "Giá sản phẩm");

        return isImageCorrect && isNameCorrect && isDescriptionCorrect && isBranchCorrect && isPriceCorrect;
    }

    //Cuộn dọc
    public void swipeToExactPosition(int startX, int startY, int endY, int duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // Chạm vào tọa độ (startX, startY)
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Vuốt đến tọa độ (startX, endY) với tốc độ được điều chỉnh
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(duration), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }
    
    // Trượt ngang
    public void swipeHorizontally(int startX,int endX, int startY, int duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // Chạm vào tọa độ (startX, startY)
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        
        // Vuốt ngang từ (startX, startY) đến (endX, startY)
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(duration), PointerInput.Origin.viewport(), endX, startY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    
    
    
    /*============= Kiểm tra thông báo lỗi checkbox=====================*/
    public boolean checkToastMessage(String expectedMessage) {
        return Allure.step("Kiểm tra thông báo hiển thị", () -> {
            try {
            	
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast")));
                WebElement toast = driver.findElement(By.xpath("//android.widget.Toast"));
                String actualMessage = toast.getText();

                if (actualMessage.equals(expectedMessage)) {
                    Allure.step("Toast hiển thị đúng: " + actualMessage);
                    return true;
                } else {
                    Allure.step("Toast hiển thị sai! Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
                    return false;
                }
            } catch (Exception e) {
                Allure.step("Không tìm thấy thông báo lỗi.");
                return false;
            }
        });
    }
    
    public boolean verifyInventorySorted(boolean ascending) {
        String direction = ascending ? "tăng dần" : "giảm dần";
        return Allure.step("Kiểm tra tồn kho hiển thị theo thứ tự " + direction, () -> {
            try {
                int previousInventory = ascending ? 0 : Integer.MAX_VALUE;
                int productIndex = 1;
                boolean allValid = true;

                while (true) {
                    List<WebElement> inventories = listInventory;
                    List<WebElement> names = driver.findElements(By.xpath(
                        "//android.view.ViewGroup[contains(@resource-id, '_product_name_and_price_label')]//android.widget.TextView[1]"
                    ));

                    for (int i = 0; i < Math.min(inventories.size(), names.size()); i++) {
                        String name = names.get(i).getText().trim();
                        String invText = inventories.get(i).getText().replaceAll("[^0-9]", "");
                        int currentInventory = invText.isEmpty() ? 0 : Integer.parseInt(invText);

                        boolean isValid = ascending 
                            ? currentInventory >= previousInventory 
                            : currentInventory <= previousInventory;

                        if (!isValid) {
                            Allure.step("Sản phẩm " + productIndex + ": " + name +
                                " | Tồn kho: " + currentInventory +
                                (ascending ? " < " : " > ") + previousInventory + " => Không hợp lệ");
                            allValid = false;
                        } else {
                            Allure.step("Sản phẩm " + productIndex + ": " + name +
                                " | Tồn kho: " + currentInventory +
                                (ascending ? " >= " : " <= ") + previousInventory + " =>  Hợp lệ");
                        }

                        previousInventory = currentInventory;
                        productIndex++;
                    }

                    if (isElementVisible(lastPage)) break;

                    utils.scrollDown();
                    Thread.sleep(1000);
                }

                return allValid;

            } catch (Exception e) {
                Allure.step("Lỗi kiểm tra sắp xếp tồn kho (" + direction + "): " + e.getMessage());
                return false;
            }
        });
    }



    public boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyProductIsHidden(String name) {
        return Allure.step("Kiểm tra sản phẩm đã được ẩn: " + name, () -> {
            List<WebElement> productCards = driver.findElements(By.xpath("//android.view.ViewGroup[contains(@resource-id, '_product_item_cell')]"));

            for (WebElement card : productCards) {
                try {
                    WebElement nameElement = card.findElement(By.xpath(".//android.view.ViewGroup[contains(@resource-id, '_product_name_and_price_label')]//android.widget.TextView[1]"));

                    if (nameElement.getText().trim().equals(name)) {
                        Allure.step("Sản phẩm vẫn còn hiển thị: " + name);
                        return false;
                    }
                } catch (Exception e) {
                    
                }
            }

            Allure.step("Sản phẩm đã được ẩn thành công: " + name);
            return true;
        });
    }
}
