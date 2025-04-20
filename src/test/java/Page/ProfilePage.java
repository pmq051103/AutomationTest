package Page;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ProfilePage {
	 private AndroidDriver driver;
	 private WebDriverWait wait;
	 private Actions ac;
	 private SoftAssert softAssert = new SoftAssert();
	    
	 @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Tên\")")   
	 WebElement lblName;
	 
	 @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Bio\")")
	 WebElement lblBio;
	 
	 @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Giới tính\")")
	 WebElement lblGender;
	 
	 @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Ngày sinh\")")
	 WebElement lblBirthDay;
	 
	 @AndroidFindBy(id = "com.shopee.vn:id/edittext")
	 WebElement inputName;
	 
	 
	    
	 public ProfilePage(AndroidDriver driver) {
	     this.driver = driver;
	     this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	     ac = new Actions(driver);
	     softAssert = new SoftAssert();
	     PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	 }
}
