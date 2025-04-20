package Page;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
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

public class CartPage {
	private AndroidDriver driver;
	private WebDriverWait wait;
	private Actions ac;
	private SoftAssert softAssert;
	
	@AndroidFindBy(xpath = "(//android.widget.TextView[@resource-id=\"labelItemName\"])")
	public List<WebElement> productNames;

	@AndroidFindBy(xpath = "//android.view.ViewGroup[starts-with(@resource-id, 'sectionItemRow_')]//android.widget.TextView[starts-with(@text, '₫') and @index='0']")
	List<WebElement> productPrices;

	@AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"labelSubTotalPrice\")")
	WebElement totalPrice;

	@AndroidFindBy(xpath = "//android.widget.EditText")
	List<WebElement> quantity;

	@AndroidFindBy(xpath = "(//android.widget.TextView[@resource-id=\"labelVariation\"])")
	List<WebElement> optionSelected;

	@AndroidFindBy(xpath = "(//android.view.ViewGroup[@resource-id=\"buttonCheckBoxNotChecked\"])")
	List<WebElement> checkboxNotChecked;

	@AndroidFindBy(xpath = "(//android.view.ViewGroup[@resource-id=\"buttonCheckBoxChecked\"])")
	List<WebElement> checkboxChecked;
	
	@AndroidFindBy(uiAutomator = "new UiSelector().text(\"Xóa\")")
	WebElement btnDelete;
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.TextView[1]")
	WebElement lblCartEmpty;
	
	@AndroidFindBy(xpath = "(//android.widget.TextView[@text=\"Sửa\"])[1]")
	WebElement btnEdit;
	
	@AndroidFindBy(uiAutomator  = "new UiSelector().text(\"Lưu vào Đã thích\")")
	WebElement btnAddFavorite;
	
    @AndroidFindBy(className = "android.widget.Toast")
    WebElement toastNotification;
    
    
	public CartPage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		ac = new Actions(driver);
		softAssert = new SoftAssert();
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}
	
	public CartPage clickBtnDelete() {
		Allure.step("Nhấn nút xóa");
		wait.until(ExpectedConditions.elementToBeClickable(btnDelete)).click();
		return this;
	}
	
	public CartPage clickBtnAddFavorite() {
		Allure.step("Nhấn nút lưu vào đã thích");
		wait.until(ExpectedConditions.elementToBeClickable(btnAddFavorite)).click();
		return this;
	}
	
	public CartPage clickBtnEdit() {
		Allure.step("Nhấn nútsửa");
		wait.until(ExpectedConditions.elementToBeClickable(btnEdit)).click();
		return this;
	}
	
	public boolean verifyProductDetails(String expectedName, String expectedOption, int expectedQuantity) {
		return Allure.step("Kiểm tra thông tin sản phẩm trong giỏ hàng", () -> {
			boolean isMatched = true;

			// Lấy tên sản phẩm thực tế
			String actualName = productNames.get(0).getText().trim();
			if (actualName.startsWith("0")) {
				actualName = actualName.replaceFirst("^0+", "");
			}
			String actualOption = optionSelected.get(0).getText().trim();
			int actualQuantity = Integer.parseInt(quantity.get(0).getText().trim());

			// Kiểm tra tên sản phẩm
			if (!actualName.equals(expectedName)) {
				Allure.addAttachment("Sai Tên Sản Phẩm", "Mong đợi: " + expectedName + " | Thực tế: " + actualName);
				isMatched = false;
			} else {
				Allure.step("Tên sản phẩm chính xác: " + actualName);
			}

			// Kiểm tra tùy chọn sản phẩm
			if (!actualOption.equals(expectedOption)) {
				Allure.addAttachment("Sai Tùy Chọn", "Mong đợi: " + expectedOption + " | Thực tế: " + actualOption);
				isMatched = false;
			} else {
				Allure.step("Tùy chọn sản phẩm chính xác: " + actualOption);
			}

			// Kiểm tra số lượng sản phẩm
			if (actualQuantity != expectedQuantity) {
				Allure.addAttachment("Sai Số Lượng", "Mong đợi: " + expectedQuantity + " | Thực tế: " + actualQuantity);
				isMatched = false;
			} else {
				Allure.step("Số lượng sản phẩm chính xác: " + actualQuantity);
			}

			return isMatched;
		});
	}

	/*
	 * private void checkInputValue(String fieldName, String actualValue, String
	 * expectedValue, SoftAssert softAssert) { Allure.step("Kiểm tra " + fieldName,
	 * () -> { if (!expectedValue.equals(actualValue)) {
	 * Allure.addAttachment("Thông báo lỗi " + fieldName, "Mong đợi: " +
	 * expectedValue + " | Thực tế: " + actualValue); } else { Allure.step(fieldName
	 * + " khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue); }
	 * softAssert.assertEquals(actualValue, expectedValue, fieldName +
	 * " không khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
	 * }); }
	 */

	

	public boolean verifyLatestProductPrices(String productName1, String productName2, int quantity1, int quantity2) {
		SoftAssert softAssert = new SoftAssert();
		// Lấy danh sách sản phẩm trong giỏ hàng
		List<String> productNamesInCart = productNames.stream().map(WebElement::getText).collect(Collectors.toList());
		
		

		// Kiểm tra sản phẩm có trong giỏ hàng không
		int index1 = productNamesInCart.indexOf(productName1);
		int index2 = productNamesInCart.indexOf(productName2);


		if (index1 == -1 || index2 == -1) {
			softAssert.fail("Không tìm thấy sản phẩm trong giỏ hàng!");
			return false;
		}

		// Lấy giá sản phẩm


		// Kiểm tra và chọn checkbox nếu chưa được chọn
		if (checkboxNotChecked.size() > index1 && checkboxNotChecked.get(index1).isDisplayed()) {
			checkboxNotChecked.get(index1).click();
		}
		if (checkboxNotChecked.size() > index2 && checkboxNotChecked.get(index2).isDisplayed()) {
			checkboxNotChecked.get(index2).click();
		}
		
		// Lấy danh sách giá sản phẩm
				List<String> productPricesInCart = productPrices.stream().map(WebElement::getText).collect(Collectors.toList());
		int price1 = extractPrice(productPricesInCart.get(index1));
		int price2 = extractPrice(productPricesInCart.get(index2));
		
		// Lấy tổng tiền hiển thị trên giao diện
		int actualTotalPrice = extractPrice(totalPrice.getText());
		System.out.println("Tổng tiền hiển thị: " + actualTotalPrice);

		// Kiểm tra tổng tiền hiển thị đúng không
		int expectedTotalPrice = (price1* quantity1) + (price2 * quantity2);
		
		System.out.println("Giá: " + price1 + " SL: "+ quantity1);
		System.out.println("Giá: " + price2 + " SL: "+ quantity2);
		System.out.println("Tổng tiền mong đợi: " + expectedTotalPrice);

		Allure.step("Kiểm tra tổng tiền hiển thị", () -> {
            if (actualTotalPrice != expectedTotalPrice) {
            	Allure.addAttachment("Lỗi " ,
						"Tổng tiền hiển thị không không chính xác .Mong đợi: " + expectedTotalPrice + " | Thực tế: " + actualTotalPrice);
			} else {
				Allure.step("Tổng tiền hiển thị chính xác. Mong đợi: " + expectedTotalPrice + " | Thực tế: " + actualTotalPrice);
			}
        });

        // Kiểm tra tổng tiền hiển thị đúng không
        softAssert.assertEquals(actualTotalPrice, expectedTotalPrice,
                "Tổng tiền không đúng! Mong đợi: " + expectedTotalPrice + " | Thực tế: " + actualTotalPrice);

        softAssert.assertAll();
        return actualTotalPrice == expectedTotalPrice;
	}

	
	private int extractPrice(String priceText) {
		String normalizedPrice = priceText.replaceAll("[^0-9]", "").trim();
		return Integer.parseInt(normalizedPrice);
	}
	

	// Hàm xóa sản phẩm khỏi giỏ hàng
	public void swipeLeftToDelete(String productName) {
	    Allure.step("Xóa sản phẩm '" + productName + "' khỏi giỏ hàng", () -> {
	        try {
	            WebElement product = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='labelItemName' and contains(@text, '" + productName + "')]"));

	            // Lấy vị trí của sản phẩm
	            Point location = product.getLocation();
	            int startX = location.getX() + product.getSize().getWidth(); // Vuốt từ mép phải
	            int endX = location.getX(); // Vuốt sang trái
	            int y = location.getY() + product.getSize().getHeight() / 2; // Trung tâm chiều cao
	            
	            Allure.step("Trượt sản phẩm sang trái");
	            // Thực hiện vuốt ngang để xóa sản phẩm
	            swipeHorizontally(startX, endX, y, 500);
	            Thread.sleep(500);

	            // Nhấn nút xóa
	            clickBtnDelete();
	            Thread.sleep(1000);

	            System.out.println("Đã xóa sản phẩm: " + productName);
	        } catch (Exception e) {
	            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
	        }
	    });
	}

	public boolean isProductInCart(String productName) {
	    return Allure.step("Kiểm tra sản phẩm '" + productName + "' sau khi xóa", () -> {
	        	List<WebElement> elements = driver.findElements(By.xpath("//android.widget.TextView[@resource-id='labelItemName' and contains(@text, 'Mũ lưỡi trai trơn nam nữ')]"));

	        	if (elements.isEmpty()) {
	        	    Allure.step("Sản phẩm '" + productName + "' đã được xóa khỏi giỏ hàng.");
	        	    return true;
	        	} else {
	        	    Allure.addAttachment("Kết quả", "Lỗi: Sản phẩm '" + productName + "' vẫn còn tồn tại trong giỏ hàng!");
	        	    return false; 
	        	}
	    });
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
    
    public boolean isProductInCartBeforeDelete(String productName) {
        try {
            WebElement product = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='labelItemName' and contains(@text, '" + productName + "')]"));
            Allure.step("Tìm sản phẩm: "+ product);
            return product.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    
    public void clearCart(AndroidDriver driver) {
        try {
            Allure.step("Tiến hành xóa toàn bộ sản phẩm trong giỏ hàng", () -> {
                while (!productNames.isEmpty()) {
                    WebElement product = productNames.get(0); // Luôn xóa sản phẩm đầu tiên
                    String productName = product.getText(); // Lấy tên sản phẩm
                    
                    swipeHorizontally(driver, product);
                    
                    if (btnDelete.isDisplayed()) {
                        btnDelete.click();
                        Allure.step("Đã xóa sản phẩm: " + productName);
                    } else {
                        Allure.addAttachment("Lỗi", "Không tìm thấy nút xóa cho sản phẩm: " + productName);
                    }

                    Thread.sleep(1000); // Chờ giao diện cập nhật
                }
            });

        } catch (Exception e) {
            Allure.addAttachment("Lỗi", "Gặp lỗi khi xóa giỏ hàng: " + e.getMessage());
        }
    }
    
    


    
    public void swipeHorizontally(AndroidDriver driver, WebElement element) {
        // Lấy tọa độ của sản phẩm
        int startX = element.getLocation().getX() + element.getSize().getWidth() - 10; // Điểm bắt đầu
        int endX = element.getLocation().getX() + 10; // Vuốt sang trái đến điểm này
        int startY = element.getLocation().getY() + (element.getSize().getHeight() / 2); // Giữ ở giữa sản phẩm

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, startY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }
    
    public boolean verifyCartEmptyMessage(String expectedMessage) {
        return Allure.step("Kiểm tra thông báo giỏ hàng trống", () -> {
            try {
                // Chờ phần tử lblCartEmpty hiển thị
                wait.until(ExpectedConditions.visibilityOf(lblCartEmpty));

                // Lấy nội dung thông báo thực tế
                String actualMessage = lblCartEmpty.getText();


                System.out.println("Thông báo giỏ hàng: " + actualMessage);

                // Kiểm tra thông báo thực tế có khớp với mong đợi không
                boolean result = actualMessage.equals(expectedMessage);
                if (result) {
                    Allure.step("Thành công. Thông báo mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
                } else {
                    throw new AssertionError("Thông báo không khớp! Mong đợi: " + expectedMessage + " | Thực tế: " + actualMessage);
                }

                return result;
            } catch (Exception e) {              
                Allure.addAttachment("Lỗi khi kiểm tra thông báo giỏ hàng", e.getMessage());
                return false;
            }
        });     
    }
    
    public String getProductName() {
    	return productNames.get(0).getText();
    }
    
    public CartPage clickProduct() {
    	Allure.step("Nhấn chọn sản phẩm đã có trong giỏ hàng");
    	wait.until(ExpectedConditions.elementToBeClickable(productNames.get(0))).click();
    	return this;
    }
    
    public String getOption() {
    	return optionSelected.get(0).getText();
    }
    
    public int getQuantity() {
    	return Integer.parseInt(quantity.get(0).getText());
    }
    
    public boolean verifyUpdatedQuantity(String productName, int expectedQuantity) {
        return Allure.step("Kiểm tra số lượng sản phẩm sau khi thêm", () -> {
            int actualQuantity = getQuantityByProductName(productName);

            Allure.step("Số lượng mong đợi: " + expectedQuantity + " | Thực tế: " + actualQuantity);

            if (actualQuantity == expectedQuantity) {
                Allure.step("✅ Số lượng sản phẩm đã cập nhật đúng.");
                return true;
            } 
            
            Allure.step("Số lượng sản phẩm không đúng!");
            throw new AssertionError("Số lượng sản phẩm không đúng! Mong đợi: " 
                                     + expectedQuantity + " | Thực tế: " + actualQuantity);
        });
    }

    public int getQuantityByProductName(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (productNames.get(i).getText().equalsIgnoreCase(productName)) {
                return Integer.parseInt(quantity.get(i).getText());
            }
        }
        throw new NoSuchElementException("Không tìm thấy sản phẩm trong giỏ: " + productName);
    }

    
    
    public boolean verifyAndCheckProduct(String productName) {
        return Allure.step("Tìm và chọn sản phẩm: " + productName, () -> {
            // Lấy danh sách tên sản phẩm trong giỏ hàng
            List<String> productNamesInCart = productNames.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            // Tìm vị trí sản phẩm trong danh sách
            int index = productNamesInCart.indexOf(productName);

            if (index == -1) {
                // Nếu không tìm thấy trên màn hình hiện tại, thử cuộn xuống
                while (index == -1) {
                    Allure.step("Cuộn màn hình để tìm sản phẩm", () -> {
                    	swipeToExactPosition(562, 1552, 772, 1500);
                    });

                    productNamesInCart = productNames.stream()
                            .map(WebElement::getText)
                            .collect(Collectors.toList());
                    index = productNamesInCart.indexOf(productName);
                }

                if (index == -1) {
                    Allure.step("Không tìm thấy sản phẩm: " + productName);
                    return false;
                }
            }

            // Kiểm tra nếu checkbox chưa được chọn thì chọn
          
                if (checkboxNotChecked.size() > index && checkboxNotChecked.get(index).isDisplayed()) {
                    checkboxNotChecked.get(index).click();
                    Allure.step("Đã chọn check box sản phẩm: " + productName);
                    return true;
                } else {
                    Allure.step("Sản phẩm đã được chọn trước đó: " + productName);
                }
                return false;
            });
    }

    public boolean confirmToast(String toastNotificationActual) {
        return Allure.step("Kiểm tra thông báo Toast", () -> {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

                WebElement toastElement = driver.findElement(By.xpath("//android.widget.Toast[1]"));
                String toast = toastElement.getText();
                System.out.println("Toast: " + toast);

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
}
