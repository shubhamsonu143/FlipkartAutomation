package org.automation_fk;

import java.time.Duration;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

public class Test_FK_Product {
	private WebDriver driver;

	@BeforeClass
	public void setUp() {
		// Set system property to point to the location of the EdgeDriver executable
		System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
		// Initialize EdgeDriver
		driver = new EdgeDriver();
		driver.manage().window().maximize();
		// Navigate to a webpage
		driver.get("https://www.flipkart.com/");
	}

	@Test
	private void login() throws InterruptedException {
		//Pointing to the search box to input the values
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
		WebElement Search_Box = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(
				"//*[@id=\"container\"]/div/div[1]/div/div/div/div/div[1]/div/div[1]/div/div[1]/div[1]/header/div[1]/div[2]/form/div/div/input"))));
		// Inputting the value 'Laptop' into the search box.
		Search_Box.sendKeys("Laptop");
		// After entering the values in the search box Click Enter
		Search_Box.sendKeys(Keys.ENTER);
		WebElement Product = wait
				.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("(//div[@class='_13oc-S'])[1]"))));
		String parentWindowHandle = driver.getWindowHandle();
		Product.click();
		// Wait for new window or tab to open
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		// Switch to the new window
		Set<String> windowHandles = driver.getWindowHandles();
		String childWindowHandle = null;
		for (String handle : windowHandles) {
			if (!handle.equals(parentWindowHandle)) {
				childWindowHandle = handle;
				break;
			}
		}
		// Switch to the new window
		driver.switchTo().window(childWindowHandle);
		// Choosing a product from the list of available products.
		WebElement Product_name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("B_NuCI")));
		String Product_text = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].childNodes[0].nodeValue", Product_name);
		System.out.println("Product Name text : " + Product_text);
		//Adding product into the cart
		WebElement add_to_cart = wait.until(ExpectedConditions
				.visibilityOf(driver.findElement(By.xpath("//button[@class='_2KpZ6l _2U9uOA _3v1-ww']"))));
		add_to_cart.click();
		System.out.println("Upto add to cart btn done");
		try {
			WebElement Cart_Button = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Cart']")));
			if (Cart_Button.isDisplayed()) {
				Cart_Button.click();
			}
		} catch (Exception e) {
			System.out.println("Error Occured : " + e);
		}
		// Confirming the selected product and ensuring it matches the item in the cart.
		WebElement until = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_2-uG6-']")));
		String Cart_Text = until.getText();
		System.out.println("Product in Cart Text : " + Cart_Text);
		Assert.assertEquals(Cart_Text, Product_text, "Product names do not match");
		System.out.println("Product matches the item in the cart : " + Product_text);
		WebElement place_order_btn = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='_2KpZ6l _2ObVJD _3AWRsL']")));
		//scroll down
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true)", place_order_btn);
		// Clicking the place order button
		place_order_btn.click();
	}

	@AfterTest
	private void tearDown() {
		driver.quit();
	}
}
