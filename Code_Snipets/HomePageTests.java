import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
public class HomePageTests {
	static HomePage navigator;
	static ProductPage product;
	static WebDriver driver;
	

//setup chromedriver
	@BeforeSuite
	public static void setup()     
    {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ExpertBooK\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        navigator =  new HomePage(driver);
        product =  new ProductPage(driver);
        //maximize window
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

// navigate to home page and select an item and add it to cart 
    @Test 
    public void ShouldAddToCart()
    {
    	navigator.navigateToHomePage();
    	this.navigator.clickSearchItem();
    	this.navigator.insertSearchTerm("hp");
    	navigator.waitForLoaderToDisappear();
    	//System.out.println("hp clicked");
    	this.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    	this.navigator.clickOnSearchResults(1);
    	driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    	this.product.clickOnAddToCart();	
    }
 // validate the title of the item 
    @Test 
    public void ValidatProductTitile()
    {
    	String actualString = driver.findElement(By.cssSelector("#Description > h1")).getText();
    	assertTrue(actualString.contains("HP"));   
    }
    
 // validate the counter of item in the cart 
    @Test 
    public void ValidateCartCounter()
    {
    	String actualString = driver.findElement(By.cssSelector("#shoppingCartLink > span")).getText();
    	assertTrue(actualString.equals("1"));   
    	
    }
    
 
// close browser after finish    
    @AfterTest(alwaysRun=true)
    public void teardown() throws IOException {
	   driver.quit();
    }


}
