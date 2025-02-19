package Page;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
import io.qameta.allure.Step;

public class SearchPage {
    private AndroidDriver driver;
    private WebDriverWait wait;
    private Actions ac;
    private SoftAssert softAssert = new SoftAssert();

    @AndroidFindBy(xpath = "//android.widget.EditText[1]")
    WebElement inputSearch;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(2)")
    WebElement btnSearch;

    @AndroidFindBy(xpath="//android.view.ViewGroup[@index='2']//android.widget.TextView[contains(@text, '!')]")
    List<WebElement> nameProducts;
    
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Giá\")")
    WebElement optionPrice;

    public SearchPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    

    
    public List<String> getAllProductsByScrolling() {
        Set<String> productSet = new HashSet<>(); // Dùng HashSet để tránh trùng lặp sản phẩm
        List<String> productList = new ArrayList<>();
        int previousSize = 0; // Lưu số lượng sản phẩm trước đó
        final int MAX_PRODUCTS = 15; // Giới hạn số sản phẩm cần lấy

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

            // Kiểm tra nếu không có sản phẩm mới thì dừng lại
            if (productList.size() == previousSize) {
                break;
            }

            previousSize = productList.size();

            scrollDown();
        }

        return productList;
    }

    
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
    
    //Kiểm tra xem danh sách sản phẩm có đúng với tiêu chí tìm kiếm không
    public boolean verifyProductsContainSearchName(String searchName) {
        List<String> allProducts = getAllProductsByScrolling();
        AtomicBoolean containsProduct = new AtomicBoolean(false);

        return Allure.step("Kiểm tra danh sách sản phẩm:", () -> {
            allProducts.forEach(product -> {
                if (product.contains(searchName.toLowerCase())) {
                    Allure.step("✔ Sản phẩm: \"" + product + "\" có chứa thông tin tìm kiếm: \"" + searchName + "\"");
                    containsProduct.set(true);
                } else {
                    Allure.attachment("Lỗi","❌ LỖI: Sản phẩm: \"" + product + "\" KHÔNG chứa thông tin tìm kiếm: \"" + searchName + "\"");
                }
            });
            return containsProduct.get();
        });
    }
    
    // Kiểm tra xem đã click vào bộ lọc giá chưa
    @Step("Kiểm tra trạng thái của bộ lọc giá")
    public boolean isOptionPriceSelected() {
        try {
            return Allure.step("Xác minh click vào bộ lọc giá", () -> {
                // Chụp ảnh trước khi click
                File beforeScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Allure.addAttachment("Ảnh trước khi click", new ByteArrayInputStream(Files.readAllBytes(beforeScreenshot.toPath())));

                clickOptionPrice();
                Thread.sleep(500); // Chờ giao diện cập nhật

                // Chụp ảnh sau khi click
                File afterScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Allure.addAttachment("Ảnh sau khi click", new ByteArrayInputStream(Files.readAllBytes(afterScreenshot.toPath())));

                // So sánh ảnh
                BufferedImage beforeImage = ImageIO.read(beforeScreenshot);
                BufferedImage afterImage = ImageIO.read(afterScreenshot);
                boolean isDifferent = areImagesDifferent(beforeImage, afterImage);

                if (!isDifferent) {
                    Allure.addAttachment("Ảnh không thay đổi (Lỗi)", new ByteArrayInputStream(Files.readAllBytes(afterScreenshot.toPath())));
                    throw new AssertionError("❌ LỖI: Bộ lọc giá chưa được chọn!");
                }

                Allure.step("✅ Đã click vào bộ lọc giá");
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }






    // Hàm so sánh hai ảnh pixel-by-pixel
    private boolean areImagesDifferent(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();

        // Kiểm tra kích thước ảnh có giống nhau không
        if (width1 != width2 || height1 != height2) {
            return true;
        }

        // Duyệt từng pixel để so sánh
        for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return true; // Nếu có pixel khác nhau, ảnh đã thay đổi
                }
            }
        }
        return false; // Ảnh giống nhau
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


}
