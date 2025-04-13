package Page;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;


import Data.DataReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.listeners.AllureTestListener;

public class SearchPage {
    private AndroidDriver driver;
    private WebDriverWait wait;
    private Actions ac;
    private SoftAssert softAssert = new SoftAssert();

    @AndroidFindBy(xpath = "//android.widget.EditText[1]")
    WebElement inputSearch;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(2)")
    WebElement btnSearch;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Giá\")")
    WebElement optionPrice;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Lọc\")")
    private WebElement filter ;
    
    @AndroidFindBy(xpath = "//androidx.recyclerview.widget.RecyclerView/android.widget.ImageView[1]")
    private WebElement image;
    
    @AndroidFindBy(xpath="//android.view.ViewGroup[@index='0']//android.widget.TextView[@index='0' and starts-with(@text, '₫') and matches(@text, '^₫[0-9,.]+$')]")
    List<WebElement> productPrice;
    
    @AndroidFindBy(xpath="//android.view.ViewGroup[@index='1']//android.widget.TextView[starts-with(@text, '!')]")
    List<WebElement> nameProducts;
    
    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id='com.shopee.vn:id/DRE_VIEW_CONTAINER_ID']")
    List<WebElement> productContainers;

    
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@index='1']")
    List<WebElement> productItems;
    
    @AndroidFindBy(xpath = "//android.widget.TextView[starts-with(@resource-id, 'labelItemCardItemName_')]")
    List<WebElement> itemNames;
    
    @AndroidFindBy(xpath = "(//android.widget.FrameLayout[@resource-id=\"com.shopee.vn:id/DRE_VIEW_CONTAINER_ID\"])[position() >= 4]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
    List<WebElement> Suggest;
    
    @AndroidFindBy(uiAutomator  = "new UiSelector().text(\"Đánh giá\")")
    WebElement evaluate;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Nơi Bán\")")
    WebElement whereBuy;
    
    @AndroidFindBy(xpath = "(//android.widget.FrameLayout[@resource-id='com.shopee.vn:id/DRE_VIEW_CONTAINER_ID'])[1]/android.view.ViewGroup//android.widget.TextView[1]")
    WebElement listProductEmpty;
    public SearchPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        ac = new Actions(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public SearchPage sendKeyInputSearch(String nameProduct) {
        Allure.step("Nhập tên sản phẩm: " + nameProduct);
        wait.until(ExpectedConditions.visibilityOf(inputSearch)).sendKeys(nameProduct);
        return this;
    }
    
    public SearchPage chooseImage() {
    	Allure.step("Chọn ảnh từ thư viện");
    	wait.until(ExpectedConditions.elementToBeClickable(image)).click();
    	return this;
    }

    public SearchPage clickBtnSearch() {
        Allure.step("Nhấn nút tìm");
        wait.until(ExpectedConditions.elementToBeClickable(btnSearch)).click();
        return this;
    }
    
    
    public String clickOptionLocation() throws InterruptedException {
        int x = 357, y = 1021;
        
        //tapAtPosition(x, y);
        
        //Thread.sleep(500); 
        
        String clickedText = getTextAtPosition(x, y);
        Allure.step("Nhấn chọn option "+ clickedText);
        Thread.sleep(1000);
        tapAtPosition(815, 2157);
        Allure.step("Nhấn nút áp dụng");
        
        return clickedText;
    }
    
    public SearchPage clickOptionWhereBy() throws InterruptedException {
    	Allure.step("Nhấn chọn option Nơi bán");
    	Thread.sleep(2000);
    	tapAtPosition(357, 1021);
    	return this;
    }
    
    public String getTextAtPosition(int x, int y) {
        List<WebElement> textElements = driver.findElements(By.xpath("//android.widget.TextView"));
        
        WebElement closestElement = null;
        double minDistance = Double.MAX_VALUE;

        for (WebElement element : textElements) {
            Point location = element.getLocation();
            double distance = Math.sqrt(Math.pow(location.x - x, 2) + Math.pow(location.y - y, 2));

            if (distance < minDistance) {
                minDistance = distance;
                closestElement = element;
            }
        }

        if (closestElement != null) {
            tapAtPosition(closestElement.getLocation().x, closestElement.getLocation().y);
            return closestElement.getText();
        } else {
            return "Không tìm thấy phần tử gần tọa độ";
        }
    }

    public SearchPage searchByNameAS(String name) {
    	sendKeyInputSearch(name);
    	return clickBtnSearch();
    }
    
    public SearchPage clickOptionPriceAscending() {
        Allure.step("Nhấn vào lọc giá tăng dần");
        wait.until(ExpectedConditions.elementToBeClickable(optionPrice)).click();
        return this;
    }
    
    public SearchPage clickOptionPriceDescending() throws InterruptedException {
        Allure.step("Nhấn vào lọc giá tăng dần");
        wait.until(ExpectedConditions.elementToBeClickable(optionPrice)).click();
        Thread.sleep(3000);
        optionPrice.click();
        return this;
    }

    public SearchPage clickFilter() {
        Allure.step("Nhấn mở bộ lọc");
        wait.until(ExpectedConditions.elementToBeClickable(filter)).click();
        return this;
    }
    
    public SearchPage clickProduct(int i) {
        Allure.step("Nhấn chọn sản phẩm thứ " + (i + 1));
        
        if (nameProducts.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy sản phẩm nào trong danh sách!");
        }

        if (i >= nameProducts.size()) {
            throw new IndexOutOfBoundsException("Không thể chọn sản phẩm thứ " + (i + 1) + 
                " vì danh sách chỉ có " + nameProducts.size() + " sản phẩm!");
        }

        WebElement product = nameProducts.get(i);
        wait.until(ExpectedConditions.elementToBeClickable(product)).click(); 
        return this;
    }

    public String getSpan() {
    	return listProductEmpty.getText();
    }
    
    public List<String> getProductListBySearchName(String searchName) throws IOException {
        // Xây dựng đường dẫn file dựa trên tên sản phẩm
        String filePath = "DataFile/" + searchName.replace(" ", "") + ".csv"; 
        List<String[]> csvData = DataReader.getCSVData(filePath, 1);
        
        // Chuyển đổi từ List<String[]> thành List<String>
        List<String> productList = new ArrayList<>();
        for (String[] row : csvData) {
            productList.add(row[0].trim());
        }
        
        return productList;
    }

    
    public boolean verifyProductsContainSearchName(String searchName, List<WebElement> productElements) throws IOException {
        // Lấy danh sách từ khóa mong đợi từ file CSV và chuẩn hóa
        List<String> expectedKeywords = getProductListBySearchName(searchName)
                .stream()
                .map(String::trim)
                .map(keyword -> normalizeText(keyword.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("Expected Keywords: " + expectedKeywords);

        Set<String> seenProducts = new HashSet<>();
        AtomicInteger previousSize = new AtomicInteger(0);
        final int MAX_PRODUCTS = 14;

        return Allure.step("Kiểm tra danh sách sản phẩm theo từ khóa: " + searchName, () -> {
            while (seenProducts.size() < MAX_PRODUCTS) {
                for (WebElement product : productElements) { // Dùng danh sách truyền vào
                    // Lấy tên sản phẩm và chuẩn hóa
                    String productName = normalizeText(product.getText().toLowerCase());

                    System.out.println("Product Name: " + productName);
                    if (seenProducts.contains(productName)) {
                        continue;
                    }
                    seenProducts.add(productName);

                    boolean isValid = Allure.step("Kiểm tra sản phẩm: " + productName, () -> {
                        boolean result = expectedKeywords.stream().anyMatch(keyword ->
                                productName.contains(keyword));

                        if (result) {
                            Allure.step("Sản phẩm hợp lệ: " + productName);
                        } else {
                        	Allure.attachment("Lỗi", "Sản phẩm không hợp lệ: " + productName);
                            softAssert.assertEquals("Lỗi", "Sản phẩm không hợp lệ: " + productName);
                        }

                        return result;
                    });

                    if (!isValid) {
                        return false;
                    }

                    if (seenProducts.size() >= MAX_PRODUCTS) {
                        Allure.step("Tất cả sản phẩm hiển thị đều hợp lệ.");
                        return true;
                    }
                }

                if (seenProducts.size() == previousSize.get()) {
                    break;
                }

                previousSize.set(seenProducts.size());
                scrollDown();
            }

            Allure.step("Tất cả sản phẩm hiển thị đều hợp lệ.");
            return true;
        });
    }
    
    public boolean verifyProductsSearchByName(String searchName) throws IOException {
    	return verifyProductsContainSearchName(searchName, nameProducts);
    }
    
    public boolean verifyProductsSearchByImage(String searchName) throws IOException {
    	return verifyProductsContainSearchName(searchName, itemNames);
    }


    private String normalizeText(String text) {
        return text.replaceFirst("^0+", "").replaceAll("[^\\p{L}\\p{N}\\s]", "").replaceAll("\\s+", " ").trim();
    }



//    //Kiểm tra xem danh sách sản phẩm có đúng với tiêu chí tìm kiếm không
//    public boolean verifyProductsContainSearchName(String searchName) {
//        List<String> allProducts = getAllProductsByScrolling();
//        AtomicBoolean containsProduct = new AtomicBoolean(false);
//
//        return Allure.step("Kiểm tra danh sách sản phẩm:", () -> {
//            allProducts.forEach(product -> {
//                if (product.contains(searchName.toLowerCase())) {
//                    Allure.step("✔ Sản phẩm: \"" + product + "\" có chứa thông tin tìm kiếm: \"" + searchName + "\"");
//                    containsProduct.set(true);
//                } else {
//                    Allure.step("Lỗi Sản phẩm: \"" + product + "\" KHÔNG chứa thông tin tìm kiếm: \"" + searchName + "\"");
//                }
//            });
//            return containsProduct.get();
//        });
//    }
    


    
    private void scrollDown() {
        int startX = driver.manage().window().getSize().width / 2;
        int startY = (int) (driver.manage().window().getSize().height * 0.8);
        int endY = (int) (driver.manage().window().getSize().height * 0.2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    public boolean verifyProductsWithinPriceRange(int minPrice, int maxPrice) {
        return Allure.step("Kiểm tra giá sản phẩm trong khoảng [" + minPrice + " - " + maxPrice + "]", () -> {
            final int MAX_PRODUCTS = 20;
            AtomicInteger validCount = new AtomicInteger(0);
            Set<String> checkedProducts = new HashSet<>();

            do {

                List<WebElement> productContainers = driver.findElements(By.xpath(
                    "(//android.widget.FrameLayout[@resource-id=\"com.shopee.vn:id/DRE_VIEW_CONTAINER_ID\"][position() > 3])"
                    + "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
                    + "/android.view.ViewGroup/android.view.ViewGroup[2]"
                ));

                for (WebElement container : productContainers) {
                    try {
                        WebElement nameElement = container.findElement(By.xpath(".//android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.widget.TextView"));
                        WebElement priceElement = container.findElement(By.xpath(".//android.widget.TextView[starts-with(@text, '₫')]"));

                        if (!nameElement.isDisplayed() || !priceElement.isDisplayed()) {
                            continue;
                        }

                        String productName = nameElement.getText();
                        int productPrice = extractPrice(priceElement.getText());

                        if (checkedProducts.contains(productName)) {
                            continue;
                        }
                        checkedProducts.add(productName);

                        boolean isValid = Allure.step("Kiểm tra sản phẩm: " + productName, () -> {
                            if (productPrice < minPrice || productPrice > maxPrice) {
                                Allure.attachment("Lỗi", "Sản phẩm: " + productName + " có giá ₫" + productPrice + " ngoài khoảng cho phép.");
                                return false;
                            } else {
                                Allure.step(" Hợp lệ - Giá: ₫" + productPrice);
                                return true;
                            }
                        });

                        if (!isValid) {
                            return false;
                        }

                        validCount.incrementAndGet();
                        if (validCount.get() >= MAX_PRODUCTS) {
                            Allure.step("Tất cả sản phẩm đều nằm trong khoảng giá");
                            return true;
                        }
                    } catch (StaleElementReferenceException e) {
                        System.out.println("Phần tử không còn tồn tại, tải lại danh sách...");
                        break;
                    } catch (NoSuchElementException e) {
                        System.out.println("Không tìm thấy phần tử: " + e.getMessage());
                    }
                }

                scrollDown();
                Thread.sleep(2000);
            } while (validCount.get() < MAX_PRODUCTS);

            return true;
        });
    }



    
    public boolean verifyProductsSortedByAscendingPrice() {
        return verifyProductsSortedByOrder(true);
    }

    public boolean verifyProductsSortedByDescendingPrice() {
        return verifyProductsSortedByOrder(false);
    }

    
    public boolean verifyProductsSortedByOrder(boolean isAscending) {
        String orderText = isAscending ? "tăng dần" : "giảm dần";
        return Allure.step("Kiểm tra sản phẩm có được sắp xếp theo giá " + orderText, () -> {
            AtomicInteger previousPrice = new AtomicInteger(isAscending ? 0 : Integer.MAX_VALUE);
            AtomicInteger checkedCount = new AtomicInteger(0);
            final int MAX_PRODUCTS = 20;
            Set<String> checkedProducts = new HashSet<>();

            do {

                List<WebElement> productContainers = driver.findElements(By.xpath(
                    "(//android.widget.FrameLayout[@resource-id=\"com.shopee.vn:id/DRE_VIEW_CONTAINER_ID\"][position() > 3])"
                    + "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
                    + "/android.view.ViewGroup/android.view.ViewGroup[2]"
                ));

                System.out.println("Số lượng sản phẩm: " + productContainers.size());

                for (WebElement container : productContainers) {
                    try {
                        List<WebElement> nameElements = container.findElements(By.xpath(".//android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.widget.TextView"));
                        List<WebElement>  priceElements = container.findElements(By.xpath(".//android.widget.TextView[starts-with(@text, '₫')]"));
                        
                        WebElement nameElement = nameElements.get(0);
                        WebElement priceElement = priceElements.get(0);
                        
                        if (!nameElement.isDisplayed() || !priceElement.isDisplayed()) {
                            continue;
                        }

                        String productName = nameElement.getText();
                        int currentPrice = extractPrice(priceElement.getText());

                        if (checkedProducts.contains(productName)) {
                            continue;
                        }
                        checkedProducts.add(productName);

                        boolean isValid = Allure.step("Kiểm tra sản phẩm: " + productName, () -> {
                            boolean isSortedCorrectly = isAscending ? (currentPrice >= previousPrice.get()) : (currentPrice <= previousPrice.get());

                            if (checkedCount.get() > 0 && !isSortedCorrectly) {
                                Allure.attachment("Lỗi", "Sản phẩm: " + productName + " có giá ₫" + currentPrice + 
                                                  (isAscending ? " nhỏ hơn " : " lớn hơn ") + "sản phẩm trước đó ₫" + previousPrice.get());
                                return false;
                            } else {
                                Allure.step("✅ Hợp lệ - Giá: ₫" + currentPrice);
                                return true;
                            }
                        });

                        if (!isValid) {
                            return false;
                        }

                        previousPrice.set(currentPrice);
                        checkedCount.incrementAndGet();

                        if (checkedCount.get() >= MAX_PRODUCTS) {
                            Allure.step("Tất cả sản phẩm đã được sắp xếp theo giá " + orderText);
                            return true;
                        }

                    } catch (StaleElementReferenceException e) {
                        System.out.println("Phần tử không còn tồn tại, tải lại danh sách...");
                        break;
                    } catch (NoSuchElementException e) {
                        System.out.println("Không tìm thấy phần tử: " + e.getMessage());
                    }
                }

                scrollDown();
                Thread.sleep(2000);

            } while (checkedCount.get() < MAX_PRODUCTS);

            return true;
        });
    }



    



    

    // Chuyển đổi giá từ chuỗi sang số nguyên
    private int extractPrice(String rawPrice) throws NumberFormatException {
        // Cắt chuỗi để lấy giá đầu tiên (trước khoảng trắng nếu có)
        String firstPrice = rawPrice.split("\\s+")[0];

        // Chuyển đổi giá trị thành số nguyên
        return Integer.parseInt(firstPrice.replace("₫", "").replaceAll("[^0-9]", "").trim());
    }
    
    
    public SearchPage filterAS(String min, String max) throws InterruptedException {
        Thread.sleep(1000);
        Allure.step("Cuộn để tìm bộ lọc theo khoảng giá");
        swipeToExactPosition(582, 2030, 563, 1500);

        Thread.sleep(1000);
        clickAndSendKeys(351, 985, min);
        Allure.step("Nhập giá tối thiểu: " + min);

        clickAndSendKeys(802, 985, max);
        Allure.step("Nhập giá tối đa: " + max);

        if (driver.isKeyboardShown()) {
            System.out.println("Bàn phím đang mở, sẽ ẩn đi...");
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK)); 
        } else {
            System.out.println("Bàn phím đã đóng, không cần ẩn.");
        }

        Thread.sleep(1000);
        tapAtPosition(1030, 2190);
        System.out.println("Đã click nút xác nhận");
        return this;
    }
    
    public String clickFilterFeedback() throws InterruptedException {
    	int x = 353 , y =1191;
        Thread.sleep(1000);
        Allure.step("Cuộn để tìm bộ lọc theo đánh giá");
        swipeToExactPosition(582, 2030, 564, 1500);
        Thread.sleep(1000);
        swipeToExactPosition(582, 2030, 564, 1500);
        
        Thread.sleep(1000);
        String feedbackStr = getTextAtPosition(x, y);
        Allure.step("Nhấn chọn option: " + feedbackStr);
        Thread.sleep(1000);
        tapAtPosition(815, 2157);
        Allure.step("Nhấn nút áp dụng");
        return feedbackStr;
    }
    
    public String clickFilterOnSale() throws InterruptedException {
    	int x = 353 , y =1763;
        Thread.sleep(1000);
        Allure.step("Cuộn để tìm bộ lọc theo đang giảm giá");
        swipeToExactPosition(582, 2030, 564, 1500);
        Thread.sleep(1000);
        swipeToExactPosition(582, 2030, 564, 1500);
        
        Thread.sleep(1000);
        String onSaleStr = getTextAtPosition(x, y);
        Allure.step("Nhấn chọn option: " + onSaleStr);
        Thread.sleep(1000);
        tapAtPosition(815, 2157);
        Allure.step("Nhấn nút áp dụng");
        return onSaleStr;
    }
    
    public boolean verifyProductsWithinRating(String rating) {
        return Allure.step("Kiểm tra đánh giá sản phẩm: " + rating , () -> {
            final int MAX_PRODUCTS = 14;
            AtomicInteger validCount = new AtomicInteger(0);
            Set<String> checkedProducts = new HashSet<>();
            System.out.println("Trước: "+ rating);
            double expectedRating = convertToDouble(rating); 
            System.out.println("Sau: "+ expectedRating);
            
            do {
                List<WebElement> productContainers = driver.findElements(By.xpath(
                    "(//android.widget.FrameLayout[@resource-id=\"com.shopee.vn:id/DRE_VIEW_CONTAINER_ID\"][position() > 4])"
                    + "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
                    + "/android.view.ViewGroup/android.view.ViewGroup[2]"
                ));

                for (WebElement container : productContainers) {
                    try {
                        if (!container.isDisplayed()) {
                            continue;
                        }
                        List<WebElement> nameElements = container.findElements(By.xpath(".//android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.widget.TextView"));
                        List<WebElement> ratingElements = container.findElements(By.xpath(".//android.view.ViewGroup[4]/android.view.ViewGroup[1]/android.view.ViewGroup"
                                + "/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.TextView"));

                        if (nameElements.isEmpty() || ratingElements.isEmpty()) {
                            continue;
                        }

                        WebElement nameElement = nameElements.get(0);
                        WebElement ratingElement = ratingElements.get(0);

                        String productName = nameElement.getText();
                        double productRating = convertToDouble(ratingElement.getText());

                        if (checkedProducts.contains(productName)) {
                            continue;
                        }
                        checkedProducts.add(productName);

                        boolean isValid = Allure.step("Kiểm tra sản phẩm: " + productName, () -> {
                            if (productRating < expectedRating) {
                                Allure.attachment("Lỗi", "Sản phẩm " + productName + " có rating " + productRating + " thấp hơn yêu cầu.");
                                return false;
                            }
                            Allure.step("Sản phẩm " + productName +" Hợp lệ - Rating: " + productRating);
                            return true;
                        });

                        if (!isValid) {
                            return false;
                        }

                        validCount.incrementAndGet();
                        if (validCount.get() >= MAX_PRODUCTS) {
                            Allure.step("Tất cả sản phẩm đều đạt yêu cầu về rating.");
                            return true;
                        }
                    } catch (StaleElementReferenceException e) {
                        System.out.println("Phần tử không còn tồn tại, tải lại danh sách...");
                        break;
                    } catch (NoSuchElementException e) {
                        System.out.println("Không tìm thấy phần tử: " + e.getMessage());
                    }
                }

                scrollDown();
                Thread.sleep(2000);
            } while (validCount.get() < MAX_PRODUCTS);

            return true;
        });
    }


    @Step("Kiểm tra nội dung tìm kiếm")
    public boolean confirmSearchContent(String nameProduct) {
        wait.until(ExpectedConditions.visibilityOf(inputSearch));
        String nameProductActual = inputSearch.getText();

        return Allure.step("Xác nhận nội dung tìm kiếm", () -> {
            if (nameProduct.equals(nameProductActual)) {
                Allure.step("Thành công. Tên sản phẩm mong đợi: " + nameProduct + " | Thực tế: " + nameProductActual);
                return true;
            } else {
                Allure.addAttachment("Lỗi: Tên sản phẩm không khớp", "Mong đợi: " + nameProduct + " | Thực tế: " + nameProductActual);
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

    private void tapAtPosition(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Collections.singletonList(tap));
    }
    
    private void clickAndSendKeys(int x, int y, String text) throws InterruptedException {
        tapAtPosition(x, y);  
        Thread.sleep(3000);
        ac.sendKeys(text).perform();
    }
    
    public String selectRandomSuggest() {
        return Allure.step("Thực hiện chọn ngẫu nhiên 1 gợi ý", () -> {
        	 driver.hideKeyboard();
             Thread.sleep(1000);
            Random random = new Random();
            int randomIndex = random.nextInt(Suggest.size());
            WebElement selectedProduct = Suggest.get(randomIndex);
            
            WebElement textView = selectedProduct.findElement(By.xpath(".//android.widget.TextView"));
            String productName = textView.getText();

            Allure.step("Sản phẩm được chọn: " + productName);
            selectedProduct.click();

            return normalizeText(productName);
        });
    }
    
    public boolean verifyFirstProductMatchesSuggestion(String suggest) {
        return Allure.step("Kiểm tra sản phẩm có hợp lệ với gợi ý tìm kiếm", () -> {
            // Lấy tên sản phẩm đầu tiên hiển thị
            WebElement firstProduct = nameProducts.get(0);
            String firstProductName = firstProduct.getText().toLowerCase();

            // Tách gợi ý tìm kiếm thành các từ riêng biệt
            String[] keywords = suggest.toLowerCase().split("\\s+");

            // Kiểm tra xem tên sản phẩm có chứa ít nhất một từ trong gợi ý không
            boolean isValid = Arrays.stream(keywords).anyMatch(firstProductName::contains);

            // Log kết quả vào Allure
            Allure.step("So sánh: [" + firstProductName + "] với gợi ý [" + suggest + "]");
            Allure.step("Kết quả kiểm tra: " + (isValid ? "Hợp lệ" : "Không hợp lệ"));

            return isValid;
        });
    }
    
    
    
    
    public void swipeLeftFromElement() {
    	Allure.step("Vuốt sang trái để tìm bộ lọc theo nơi bán");
        int startX = evaluate.getLocation().getX() + evaluate.getSize().getWidth() - 10;
        int startY = evaluate.getLocation().getY() + (evaluate.getSize().getHeight() / 2);
        int endX = evaluate.getLocation().getX() - 400; // Vuốt về bên trái

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // Chạm vào điểm bắt đầu
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Vuốt đến điểm kết thúc với thời gian `duration`
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1500), PointerInput.Origin.viewport(), endX, startY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }
    
    
    public boolean verifyProductsWithWhereBuy(String whereBy) {
        return Allure.step("Kiểm tra nơi bán của sản phẩm là [" + whereBy + "]", () -> {
            final int MAX_PRODUCTS = 14;
            AtomicInteger validCount = new AtomicInteger(0);
            Set<String> checkedProducts = new HashSet<>();

            do {
                List<WebElement> productContainers = driver.findElements(By.xpath(
                    "(//android.widget.FrameLayout[@resource-id='com.shopee.vn:id/DRE_VIEW_CONTAINER_ID'][position() > 3])"
                    + "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
                    + "/android.view.ViewGroup/android.view.ViewGroup[2]"
                ));

                for (WebElement container : productContainers) {
                    List<WebElement> nameElements = container.findElements(By.xpath(".//android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.widget.TextView"));
                    List<WebElement> whereBuyElements = container.findElements(By.xpath(".//android.view.ViewGroup[4]/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView"));

                    if (nameElements.isEmpty() || whereBuyElements.isEmpty()) {
                        continue; 
                    }

                    WebElement nameElement = nameElements.get(0);
                    WebElement whereBuyElement = whereBuyElements.get(0);

                    if (!nameElement.isDisplayed() || !whereBuyElement.isDisplayed()) {
                        continue;
                    }

                    String productName = nameElement.getText();
                    String productWhereBuy = whereBuyElement.getText().trim();

                    if (checkedProducts.contains(productName)) {
                        continue;
                    }
                    checkedProducts.add(productName);

                    boolean isValid = Allure.step("Kiểm tra sản phẩm: " + productName, () -> {
                        if (!productWhereBuy.equalsIgnoreCase(whereBy)) {
                            Allure.attachment("Lỗi", "Sản phẩm: " + productName + " có nơi bán là [" + productWhereBuy + "] không khớp với [" + whereBy + "]");
                            return false;
                        } else {
                            Allure.step("Hợp lệ - Nơi bán: " + productWhereBuy);
                            return true;
                        }
                    });

                    if (!isValid) {
                        return false;
                    }

                    validCount.incrementAndGet();
                    if (validCount.get() >= MAX_PRODUCTS) {
                        Allure.step("Tất cả sản phẩm đều có nơi bán hợp lệ");
                        return true;
                    }
                }

                scrollDown();
                Thread.sleep(1500);
            } while (validCount.get() < MAX_PRODUCTS);

            return true;
        });
    }
    
    public boolean verifyEmptySearchResultSpan(String expectedMessage) {
        return Allure.step("Xác minh span hiển thị khi kết quả tìm kiếm rỗng", () -> {
            try {
                wait.until(ExpectedConditions.visibilityOf(listProductEmpty));

                String actualMessage = listProductEmpty.getText();
                boolean result = actualMessage.equals(expectedMessage);
                if (result) {
                    Allure.step("Thành công. Span hiển thị đúng: \"" + actualMessage + "\"");
                } else {
                    Allure.attachment("Lỗi","Span không khớp. Mong đợi: \"" + expectedMessage + "\" | Thực tế: \"" + actualMessage + "\"");
                }

                return result;
            } catch (Exception e) {
                Allure.addAttachment("Lỗi khi xác minh span hiển thị", e.getMessage());
                return false;
            }
        });
    }


    
    public static double convertToDouble(String text) {
        try {
            String numberPart = text.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numberPart);
        } catch (Exception e) {
            System.out.println("Lỗi chuyển đổi: " + e.getMessage());
            return -1;
        }
    }


}
