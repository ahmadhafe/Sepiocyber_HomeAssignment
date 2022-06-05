import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductPage extends WebActions{

    private final By productTitle = By.id("mobileDescription");
    private final By addToCart = By.name("save_to_cart");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public boolean waitForProductPage()
    {
        return super.waitForElement(true,addToCart,5);
    }

    public String  getProductTitle()
    {
        WebElement parent = super.getElement(By.id("Description"));
        WebElement productTitle = super.getSubElement(parent,By.tagName("h1"));
        return super.getElementText(productTitle);
    }

    //TODO: Add validate product title method

    public boolean clickOnAddToCart()
    {
        return super.clickOnElement(addToCart);
    }

    public int getCartCounter()
    {
        WebElement parent = super.getElement(By.id("shoppingCartLink"));
        WebElement cartCounter = super.getSubElement(parent,By.className("cart"));

        return Integer.parseInt(super.getElementText(cartCounter));
    }

    //TODO Add validate counter method
}
