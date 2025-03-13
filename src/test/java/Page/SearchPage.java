package Page;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
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
    
    @AndroidFindBy(xpath="//android.view.ViewGroup[@index='0']//android.widget.TextView[@index='0' and starts-with(@text, '₫') and matches(@text, '^₫[0-9,.]+$')]")
    List<WebElement> productPrice;
    @AndroidFindBy(xpath="//android.view.ViewGroup[@index='1']//android.widget.TextView[starts-with(@text, '!')]")
    List<WebElement> nameProducts;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@index='1']")
    List<WebElement> productItems;

    
    
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

    public SearchPage clickBtnSearch() {
        Allure.step("Nhấn nút tìm");
        wait.until(ExpectedConditions.elementToBeClickable(btnSearch)).click();
        return this;
    }
    
    public SearchPage clickOptionPrice() {
        Allure.step("Nhấn vào bộ lọc giá");
        wait.until(ExpectedConditions.elementToBeClickable(optionPrice)).click();
        return this;
    }

    public SearchPage clickFilter() {
        Allure.step("Nhấn mở bộ lọc");
        wait.until(ExpectedConditions.elementToBeClickable(filter)).click();
        return this;
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


    
    public List<String> getAllProductsByScrolling() {
        Set<String> productSet = new HashSet<>();
        List<String> productList = new ArrayList<>();
        int previousSize = 0; // Lưu số lượng sản phẩm trước đó
        final int MAX_PRODUCTS = 14; // Giới hạn số sản phẩm cần lấy
        
        while (productList.size() < MAX_PRODUCTS) { 
            List<WebElement> currentProducts = nameProducts;
            for (WebElement product : currentProducts) {
                String productName = product.getText().replace("!", "").trim().toLowerCase();
                if (!productSet.contains(productName)) {
                    System.out.println("Thêm sản phẩm: " + productName);
                    productSet.add(productName);
                    productList.add(productName);

                    if (productList.size() >= MAX_PRODUCTS) {
                        return productList;
                    }
                }
            }

            if (productList.size() == previousSize) {
                break;
            }

            previousSize = productList.size();

            scrollDown();
        }

        return productList;
    }
    
    public boolean verifyProductsContainSearchName(String searchName) throws IOException {
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

        return Allure.step("**Kiểm tra danh sách sản phẩm theo từ khóa: " + searchName, () -> {
            while (seenProducts.size() < MAX_PRODUCTS) {
                List<WebElement> currentProducts = nameProducts;
                for (WebElement product : currentProducts) {
                    // Lấy tên sản phẩm và chuẩn hóa
                    String productName = normalizeText(product.getText().toLowerCase());
                    
                    System.out.println("Product Name: " + productName);
                    // Kiểm tra nếu sản phẩm đã kiểm tra rồi thì bỏ qua
                    if (seenProducts.contains(productName)) {
                        continue;
                    }
                    seenProducts.add(productName);

                    // Kiểm tra sản phẩm với step cha
                    boolean isValid = Allure.step("Kiểm tra sản phẩm: " + productName, () -> {
                        boolean result = expectedKeywords.stream().anyMatch(keyword -> 
                            productName.contains(keyword));

                        if (result) {
                            Allure.step("Sản phẩm hợp lệ: " + productName);
                        } else {
                            Allure.step("Sản phẩm không hợp lệ: " + productName);
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

                previousSize.set(seenProducts.size()); // Cập nhật số lượng sản phẩm đã kiểm tra
                scrollDown();
            }

            Allure.step("Tất cả sản phẩm hiển thị đều hợp lệ.");
            return true;
        });
    }


    private String normalizeText(String text) {
        return text.replaceAll("[^\\p{L}\\p{N}\\s]", "").replaceAll("\\s+", " ").trim();
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
        int startY = (int) (driver.manage().window().getSize().height * 0.7);
        int endY = (int) (driver.manage().window().getSize().height * 0.3);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    public boolean verifyProductsWithinPriceRange(int minPrice, int maxPrice) {
        return Allure.step("🔍 Kiểm tra giá sản phẩm trong khoảng [" + minPrice + " - " + maxPrice + "]", () -> {
            final int MAX_PRODUCTS = 20;
            int validCount = 0;

            System.out.println("\n===== BẮT ĐẦU KIỂM TRA GIÁ =====");
            System.out.println("Khoảng giá mong muốn: [" + minPrice + " - " + maxPrice + "]");

            //boolean hasNewProducts;

            do {
                updateProductLists(); // Cập nhật danh sách sản phẩm

                List<WebElement> priceElements = driver.findElements(By.xpath("//android.view.ViewGroup[@index='0']//android.widget.TextView[@index='0' and starts-with(@text, '₫') and matches(@text, '^₫[0-9,.]+$')]"));

                for (WebElement priceElement : priceElements) {
                    try {
                        int productPrice = extractPrice(priceElement.getText());


                        if (productPrice < minPrice || productPrice > maxPrice) {
                            System.out.println("Lỗi: Sản phẩm có giá ngoài khoảng cho phép: " + productPrice);
                            throw new AssertionError("Test thất bại: Có sản phẩm không nằm trong khoảng giá!");
                        }

                        validCount++;

                        // Nếu đã kiểm tra đủ số lượng sản phẩm, dừng lại
                        if (validCount >= MAX_PRODUCTS) {
                            return true;
                        }

                    } catch (Exception e) {
                        System.out.println("Lỗi khi xử lý sản phẩm: " + e.getMessage());
                    }
                }

                scrollDown();
                Thread.sleep(2000);

            } while (validCount < MAX_PRODUCTS);

            return true;
        });
    }
    
 // Cập nhật danh sách sản phẩm sau khi cuộn để đảm bảo đồng bộ
    private void updateProductLists() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//android.view.ViewGroup[@index='1']//android.widget.TextView[starts-with(@text, '!')]")));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//android.view.ViewGroup[@index='0']//android.widget.TextView[@index='0' and starts-with(@text, '₫') and matches(@text, '^₫[0-9,.]+$')]")));
    }

    // Trả về key của sản phẩm (Tên + Giá) để tránh kiểm tra trùng
    private String getProductKey(WebElement nameElement, WebElement priceElement) {
        String productName = nameElement.getText().trim().toLowerCase();
        String productPriceText = priceElement.getText().trim();
        return productName + "-" + productPriceText;
    }

    // Cuộn xuống và chờ load sản phẩm mới
    private void scrollAndWaitForLoad() {
        scrollDown();
        try {
            Thread.sleep(1500); // Chờ nội dung load hoàn toàn
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    
    private boolean processProduct(WebElement nameElement, WebElement priceElement, int minPrice, int maxPrice, AndroidDriver driver) {
        String productName = nameElement.getText().trim().toLowerCase();
        String productPrice = priceElement.getText();

        return Allure.step("Kiểm tra sản phẩm: " + productName, () -> {
            try {
                int priceValue = extractPrice(productPrice);

                if (isPriceValid(priceValue, minPrice, maxPrice)) {
                    String successMsg = "✔ " + productName + " - Giá: " + priceValue + " nằm trong khoảng.";
                    System.out.println(successMsg);
                    Allure.step(successMsg);
                    return true;
                } else {
                    String errorMsg = "❌ LỖI: " + productName + " - Giá: " + priceValue + " KHÔNG nằm trong khoảng [" + minPrice + " - " + maxPrice + "].";
                    handleError(errorMsg, driver);
                    return false;
                }
            } catch (NumberFormatException e) {
                String parseErrorMsg = "LỖI: Không thể chuyển đổi giá sản phẩm: \"" + productPrice + "\" của sản phẩm \"" + productName + "\".";
                handleError(parseErrorMsg, driver);
                return false;
            }
        });
    }

    

    // Chuyển đổi giá từ chuỗi sang số nguyên
    private int extractPrice(String rawPrice) throws NumberFormatException {
        return Integer.parseInt(rawPrice.replace("₫", "").replaceAll("[^0-9]", "").trim());
    }

    // Kiểm tra giá có hợp lệ không
    private boolean isPriceValid(int price, int minPrice, int maxPrice) {
        return price >= minPrice && price <= maxPrice;
    }

    private void handleError(String errorMsg, AndroidDriver driver) {
        System.err.println(errorMsg);
        Allure.attachment("Lỗi", errorMsg);

        // 🛠 Gọi hàm chụp ảnh khi có lỗi
        byte[] screenshot = AllureTestListener.saveScreenshotPNG(driver);
        Allure.getLifecycle().addAttachment("Ảnh lỗi", "image/png", "png", screenshot);
    }


    
    
    public SearchPage filterAS(String min, String max) throws InterruptedException {
        Thread.sleep(1000);
        Allure.step("Cuộn để tìm bộ lọc theo khoảng giá");
        swipeToExactPosition(582, 2030, 563, 1500);

        Thread.sleep(2000);
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

        Thread.sleep(2000);
        tapAtPosition(1030, 2190);
        System.out.println("Đã click nút xác nhận");
        return this;
    }
    
    
    
    
    

    

    
    
    


    
//    // Kiểm tra xem đã click vào bộ lọc giá chưa
//    @Step("Kiểm tra trạng thái của bộ lọc giá")
//    public boolean isOptionPriceSelected() {
//        try {
//            return Allure.step("Xác minh click vào bộ lọc giá", () -> {
//                // Chụp ảnh trước khi click
//                File beforeScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//                Allure.addAttachment("Ảnh trước khi click", new ByteArrayInputStream(Files.readAllBytes(beforeScreenshot.toPath())));
//
//                clickOptionPrice();
//                Thread.sleep(500); // Chờ giao diện cập nhật
//
//                // Chụp ảnh sau khi click
//                File afterScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//                Allure.addAttachment("Ảnh sau khi click", new ByteArrayInputStream(Files.readAllBytes(afterScreenshot.toPath())));
//
//                // So sánh ảnh
//                BufferedImage beforeImage = ImageIO.read(beforeScreenshot);
//                BufferedImage afterImage = ImageIO.read(afterScreenshot);
//                boolean isDifferent = areImagesDifferent(beforeImage, afterImage);
//
//                if (!isDifferent) {
//                    Allure.addAttachment("Ảnh không thay đổi (Lỗi)", new ByteArrayInputStream(Files.readAllBytes(afterScreenshot.toPath())));
//                    throw new AssertionError("❌ LỖI: Bộ lọc giá chưa được chọn!");
//                }
//
//                Allure.step("✅ Đã click vào bộ lọc giá");
//                return true;
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//
//
//
//
//    // Hàm so sánh hai ảnh pixel-by-pixel
//    private boolean areImagesDifferent(BufferedImage img1, BufferedImage img2) {
//        int width1 = img1.getWidth();
//        int height1 = img1.getHeight();
//        int width2 = img2.getWidth();
//        int height2 = img2.getHeight();
//
//        // Kiểm tra kích thước ảnh có giống nhau không
//        if (width1 != width2 || height1 != height2) {
//            return true;
//        }
//
//        // Duyệt từng pixel để so sánh
//        for (int y = 0; y < height1; y++) {
//            for (int x = 0; x < width1; x++) {
//                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
//                    return true; // Nếu có pixel khác nhau, ảnh đã thay đổi
//                }
//            }
//        }
//        return false; // Ảnh giống nhau
//    }


    
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
        System.out.println("Clicking and sending keys at position: (" + x + ", " + y + ")");
    }
    
    private void clickAndSendKeys(int x, int y, String text) throws InterruptedException {
        tapAtPosition(x, y);  
        Thread.sleep(3000);
        ac.sendKeys(text).perform();
    }
}
