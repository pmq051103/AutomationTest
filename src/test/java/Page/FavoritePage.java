package Page;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Allure;
import utils.listeners.Utils;

public class FavoritePage {
	private AndroidDriver driver;
	private WebDriverWait wait;
	private Actions ac;
	private SoftAssert softAssert;
	private Utils untils;
	
	
	@AndroidFindBy(xpath = "//android.widget.TextView[contains(@resource-id, 'labelItemCardItemName')]")
	public List<WebElement> productNames;

	@AndroidFindBy(xpath = "//android.view.ViewGroup[starts-with(@resource-id, 'sectionItemRow_')]//android.widget.TextView[starts-with(@text, '₫') and @index='0']")
	List<WebElement> productPrices;
	
	@AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[4]/android.view.ViewGroup[1]/android.widget.ImageView")
	WebElement option;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Bỏ thích\")")
	WebElement btnUnlike;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Chỉnh sửa\")")
	WebElement btnEdit;
	
	@AndroidFindBy(uiAutomator = "(//android.widget.TextView[@text=\"Tìm sản phẩm tương tự\"])")
	List<WebElement> btnSearchProducts;
	
	@AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup[3]/android.widget.ImageView")
	List<WebElement> iconCart;
	
	@AndroidFindBy(xpath = "(//android.view.ViewGroup[@resource-id=\"buttonCheckBoxNotChecked\"])/android.widget.ImageView")
	List<WebElement> checkBoxUnlike;
	
	@AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[9]/android.view.ViewGroup")
	WebElement btnUnlikes;
	
	@AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView[2]")
	WebElement message;
	
    @AndroidFindBy(className = "android.widget.Toast")
    WebElement toast;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Không còn sản phẩm nào hiển thị\")")
    WebElement spanProductEmpty;
    
    @AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView")
	WebElement spanMessage ;
    
    @AndroidFindBy(id = "com.shopee.vn:id/buttonDefaultPositive")
    WebElement btnConfirm;
    
    @AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
    WebElement productCard;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Thêm vào Giỏ hàng\")")
    WebElement btnAddToCard;
    
    @AndroidFindBy (xpath  = "//android.view.ViewGroup[@resource-id=\"buttonActionBarView\"]/android.view.ViewGroup/android.widget.ImageView")
    WebElement btnGoToCard;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Giảm giá\")")
    WebElement filterDiscount;
    
    @AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView//android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[last()]/android.widget.TextView")
    WebElement productDiscount;
    
	public FavoritePage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		ac = new Actions(driver);
		softAssert = new SoftAssert();
		untils = new Utils(driver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}
	
	public String getProductName(WebElement productName) {
		return productName.getText();
	}
	
	public String getProductPrice(WebElement productPrice) {
		return productPrice.getText();
	}
	
	public String getMessage() {
		return message.getText();
	}
	
	public FavoritePage clickOption() {
		Allure.step("Nhấn icon tùy chọn");
		wait.until(ExpectedConditions.elementToBeClickable(option)).click();
		return this;
	}
	
	public FavoritePage clickBtnUnlike() {
		Allure.step("Nhấn nút bỏ thích");
		wait.until(ExpectedConditions.elementToBeClickable(btnUnlike)).click();
		return this;
	}
	
	public FavoritePage unlikeAS() {
		clickOption();
		return clickBtnUnlike();
	}
	
	public FavoritePage clickBtnEdit() {
		Allure.step("Nhấn nút chỉnh sửa");
		wait.until(ExpectedConditions.elementToBeClickable(btnEdit)).click();
		return this;
	}
	
	public FavoritePage clickFilterDiscount() {
		Allure.step("Nhấn chọn filter giảm giá");
		wait.until(ExpectedConditions.elementToBeClickable(filterDiscount)).click();
		return this;
	}
	
	public FavoritePage clickCheckBoxUnlike() {
		Allure.step("Chọn check box của tất cả sản phẩm");
		wait.until(ExpectedConditions.visibilityOfAllElements(checkBoxUnlike));
		for(WebElement checkbox : checkBoxUnlike) {
			checkbox.click();
		}
		return this;
	}
	
	public FavoritePage clickBtnSearchProduct(int index) {
		Allure.step("Nhấn nút tìm sản phẩm tương tự");
		wait.until(ExpectedConditions.elementToBeClickable(btnSearchProducts.get(index))).click();
		return this;
	}
	
	public FavoritePage clickIconCard(int index) {
		Allure.step("Nhấn nút icon giỏ hàng");
		wait.until(ExpectedConditions.elementToBeClickable(iconCart.get(index))).click();
		return this;
	}
	
	public FavoritePage clickBtnAddToCard() {
		Allure.step("Nhấn nút thêm vào giỏ hàng");
		wait.until(ExpectedConditions.elementToBeClickable(btnAddToCard)).click();
		return this;
	}
	
	public FavoritePage clickGoToCard() {
		wait.until(ExpectedConditions.elementToBeClickable(btnGoToCard)).click();
		return this;
	}
	
	public FavoritePage clickBtnUnlikeAll() {
		Allure.step("Nhấn nút bỏ thích tất cả");
		wait.until(ExpectedConditions.elementToBeClickable(btnUnlikes)).click();
		return this;
	}
	
	public FavoritePage selectAllCheckBoxUntilEmpty() {
	    Allure.step("Chọn tất cả checkbox sản phẩm cho đến khi không còn sản phẩm");

	    while (true) {
	        // Chờ cho checkbox hiển thị
	        wait.until(ExpectedConditions.visibilityOfAllElements(checkBoxUnlike));

	        boolean isAnyUnchecked = false; // Kiểm tra xem có checkbox chưa check không

	        for (WebElement checkbox : checkBoxUnlike) {
	            if (!checkbox.isSelected()) { // Nếu chưa được chọn, thì click vào
	                checkbox.click();
	                isAnyUnchecked = true;
	            }
	        }

	        // Nếu không còn checkbox nào cần check, dừng lại
	        if (!isAnyUnchecked) break;

	        try {
	            if (spanProductEmpty.isDisplayed()) {
	                break;
	            }
	        } catch (Exception e) {
	            swipeToExactPosition(527, 1759, 950, 1500);
	        }
	    }

	    return this;
	}
	public FavoritePage clickBtnConfirm() {
		Allure.step("Nhấn nút đồng ý");
		wait.until(ExpectedConditions.elementToBeClickable(btnConfirm)).click();
		return this;
	}
	
	public String getName() {
		return normalizeProductName(productNames.get(0).getText());
	}
	
	public boolean isProductDeleted(String productName) {
	    return Allure.step("Kiểm tra xem sản phẩm '" + productName + "' đã bị xóa khỏi danh sách yêu thích chưa", () -> {
	        boolean isDeleted = productNames.stream()
	            .map(product -> normalizeProductName(product.getText()))
	            .noneMatch(name -> name.equals(productName));

	        if (isDeleted) {
	            Allure.step("Thành công: Sản phẩm '" + productName + "' đã bị xóa khỏi danh sách yêu thích.");
	        } else {
	            Allure.step("Thất bại: Sản phẩm '" + productName + "' vẫn còn trong danh sách yêu thích.");
	            throw new AssertionError("Sản phẩm '" + productName + "' vẫn còn trong danh sách yêu thích!");
	        }
	        return isDeleted;
	    });
	}

	
	public boolean isProductInFavorite(String productNameExpected) {
	    return Allure.step("Kiểm tra xem sản phẩm '" + productNameExpected + "' có trong danh sách yêu thích không", () -> {

	        String normalizedExpected = normalizeProductName(productNameExpected);
	        System.out.println("Tên mong đợi: " + normalizedExpected);
	        printUnicode(normalizedExpected);
	        System.out.println("Tên thực tế: " + productNames.get(0).getText());
	        printUnicode(productNames.get(0).getText());
	        for (WebElement product : productNames) {
	            String productNameActual = normalizeProductName(product.getText());
	           
	            System.out.println("Tên thực tế: " + productNameActual);

	            if (productNameActual.equalsIgnoreCase(normalizedExpected)) {
	                Allure.step("Tìm thấy sản phẩm trong danh sách yêu thích!");
	                
	                return true;
	            }
	        }

	        Allure.step("Không tìm thấy sản phẩm trong danh sách yêu thích!");
	        System.out.println("Không tìm thấy sản phẩm!");
	        return false;
	    });
	}
	private void printUnicode(String str) {
	    System.out.println("Chuỗi: " + str);
	    for (int i = 0; i < str.length(); i++) {
	        char c = str.charAt(i);
	        System.out.printf("Ký tự: '%c' | Mã Unicode: %04x%n", c, (int) c);
	    }
	}
	
	private String normalizeProductName(String productName) {
	    return productName.replaceAll("[\u200B\u200C\uFEFF]", "")
	    				  .replaceAll("[\\p{Cf}]", "")
	                      .replaceAll("^\\d+", "")
	                      .replaceAll("!", "")
	                      .trim()
	                      .toLowerCase();
	}
	
	public boolean confirmSpanProductEmpty(String span) {
        return Allure.step("Kiểm tra thông báo khi xóa tất cả sản phẩm khỏi danh sách yêu tích", () -> {
            try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView")));

                    WebElement toastElement = driver.findElement(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView"));
                    String toast = toastElement.getText();

                    boolean result = toast.equals(span);
                    if (result) {
                        Allure.step("Thành công. Thông báo mong đợi: " + toast + " | Thực tế: " + span);
                    } else {
                    	throw new AssertionError("Thông báo lỗi không khớp.Mong đợi: " + span + " | Thực tế: " + toast);
                    }
                    return result;
                } catch (Exception e) {
                    
                    Allure.addAttachment("Lỗi khi lấy Toast", e.getMessage());
                    return false;
                }
            });
    }
	
	
	public boolean confirmToast(String toastNotificationActual) {
        return Allure.step("Kiểm tra thông báo toast", () -> {
            try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

                    WebElement toastElement = driver.findElement(By.xpath("//android.widget.Toast[1]"));
                    String toast = toastElement.getText();

                    // Kiểm tra thông báo Toast với giá trị mong đợi
                    boolean result = toast.equals(toastNotificationActual);
                    if (result) {
                        Allure.step("Thành công. Thông báo mong đợi: " + toast + " | Thực tế: " + toastNotificationActual);
                    } else {
                    	throw new AssertionError("Thông báo lỗi không khớp.Mong đợi: " + toastNotificationActual + " | Thực tế: " + toast);
                    }
                    return result;
                } catch (Exception e) {
                    
                    Allure.addAttachment("Lỗi khi lấy Toast", e.getMessage());
                    return false;
                }
            });
    }

    private void swipeToExactPosition(int startX, int startY, int endY, int duration) {
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
    
    
    public void clickAddToCartByProductName(String productName) {
        Allure.step("Tìm và click vào nút Add to Cart của sản phẩm: " + productName);
        
        int maxSwipes = 3;
        int swipeCount = 0;

        while (swipeCount < maxSwipes) {
            List<WebElement> productCards = driver.findElements(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"));

            for (WebElement productCard : productCards) {
                try {
                    // Tìm tên sản phẩm trong productCard
                    WebElement productTitle = productCard.findElement(By.xpath(".//android.view.ViewGroup[3]/android.widget.TextView"));
                    String actualProductName = normalizeProductName(productTitle.getText().trim());

                    if (actualProductName.equalsIgnoreCase(productName)) {
                        Allure.step("Sản phẩm được tìm thấy: " + actualProductName);
                        
                        // Click vào nút Add to Cart tương ứng
                        WebElement addToCartBtn = productCard.findElement(By.xpath(".//android.view.ViewGroup[4]/android.view.ViewGroup[3]/android.widget.ImageView"));
                        addToCartBtn.click();
                        
                        Allure.step("Đã click vào nút Add to Cart của sản phẩm: " + actualProductName);
                        return; // Thoát sau khi đã click
                    }
                } catch (Exception e) {
                    Allure.addAttachment("Lỗi khi xử lý sản phẩm: " + productName, e.getMessage());
                }
            }

            // Nếu không tìm thấy, thực hiện vuốt xuống
            Allure.step("Không tìm thấy sản phẩm, vuốt xuống để tiếp tục tìm...");
            swipeToExactPosition(500, 1600, 800, 500);
            swipeCount++;
        }

        throw new NoSuchElementException("Không tìm thấy sản phẩm có tên: " + productName + " sau khi vuốt " + maxSwipes + " lần");
    }
    
    public boolean isProductInFavorites(String productName) {
        return Allure.step("Kiểm tra sản phẩm '" + productName + "' có trong danh sách yêu thích hay không", () -> {
            String normalizedProductName = normalizeProductName(productName);

            // Lấy danh sách tên sản phẩm trong danh sách yêu thích
            List<String> productNamesInFavorites = productNames.stream()
                    .map(WebElement::getText)
                    .map(this::normalizeProductName)
                    .collect(Collectors.toList());

            System.out.println("Danh sách sản phẩm trong yêu thích: " + productNamesInFavorites);

            if (productNamesInFavorites.contains(normalizedProductName)) {
                Allure.step("Sản phẩm '" + productName + "' có trong danh sách yêu thích.");
                return true;
            }

            // Cuộn xuống để tìm sản phẩm
            swipeToExactPosition(500, 1500, 750, 1500);

            // Cập nhật danh sách sau khi cuộn
            productNamesInFavorites = productNames.stream()
                    .map(WebElement::getText)
                    .map(this::normalizeProductName)
                    .collect(Collectors.toList());


            if (productNamesInFavorites.contains(normalizedProductName)) {
                Allure.step("Sản phẩm '" + productName + "' có trong danh sách yêu thích sau khi cuộn.");
                return true;
            }

            Allure.attachment("Kết quả", "Sản phẩm '" + productName + "' không có trong danh sách yêu thích.");
            throw new AssertionError("Sản phẩm '" + productName + "' không có trong danh sách yêu thích!");
        });
    }


    public boolean verifyProductsFilterWithDiscount() {
        return Allure.step("Kiểm tra danh sách yêu thích lọc theo giảm giá", () -> {
            Set<String> checkedProducts = new HashSet<>();
            boolean hasMoreProducts = true; // Tiếp tục cuộn nếu còn sản phẩm

            while (hasMoreProducts) {
                List<WebElement> productContainers = driver.findElements(By.xpath(
                    "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[3]"
                ));

                System.out.println("Số lượng sản phẩm: " + productContainers.size());

                boolean foundValidProduct = false; // Kiểm tra có sản phẩm nào hợp lệ không

                for (WebElement productContainer : productContainers) {
                    try {
                        // Lấy tên sản phẩm
                        WebElement nameElement = productContainer.findElement(By.xpath(".//android.widget.TextView"));
                        String productName = nameElement.getText().trim();

                        // Lấy phần tử giảm giá
                        WebElement discountElement = productContainer.findElement(By.xpath(
                            ".//android.view.ViewGroup[3]/android.widget.TextView | .//android.view.ViewGroup[2]/android.widget.TextView"
                        ));

                        // Kiểm tra hiển thị
                        if (!discountElement.isDisplayed() || !nameElement.isDisplayed()) {
                            System.out.println("Sản phẩm bị ẩn, bỏ qua: " + productName);
                            continue; 
                        }

                        // Lấy giá trị giảm giá
                        String discountText = discountElement.getText().trim();
                        if (!discountText.matches("-\\d+%")) {
                            Allure.attachment("Lỗi", "Sản phẩm " + productName + " không giảm giá");
                            return false;
                        }

                        // Kiểm tra sản phẩm đã được duyệt chưa
                        if (!checkedProducts.contains(productName)) {
                            checkedProducts.add(productName);
                            Allure.step("Sản phẩm hợp lệ: " + productName + " - Giảm giá: " + discountText);
                        }

                        foundValidProduct = true; // Đánh dấu đã tìm thấy sản phẩm hợp lệ

                    } catch (NoSuchElementException e) {
                        System.out.println("Không tìm thấy phần tử giảm giá, bỏ qua sản phẩm.");
                        continue;
                    }
                }

                // Cuộn xuống nếu vẫn còn sản phẩm
                untils.scrollDown();
                Thread.sleep(1500);
                hasMoreProducts = !isProductListEmpty(); // Nếu hết sản phẩm thì dừng
            }

            return true;
        });
    }




    // Hàm kiểm tra danh sách sản phẩm đã hết hay chưa
    private boolean isProductListEmpty() {
        try {
            List<WebElement> emptyElements = driver.findElements(By.xpath("//android.widget.TextView[contains(@text, 'Không còn sản phẩm nào hiển thị')]"));
            return !emptyElements.isEmpty() && emptyElements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


}
