import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends WebActions {

    private final By menuSearchLocator = By.id("menuSearch");
    private final By loader = By.className("loader");
    private final By searchField = By.id("autoComplete");
    private final By searchResultsItem = By.className("product");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean navigateToHomePage()
    {
        return super.navigateToWebPage("http://advantageonlineshopping.com/#/");
    }

    //TODO: Change this method to validate the actual page title
    public String getTitle()
    {
        return driver.getTitle();
    }

    public boolean clickSearchItem()
    {
        boolean result = super.waitForElementToBeClickable(5, menuSearchLocator);
        if(!result)
            return false;

        return super.clickOnElement(menuSearchLocator);

    }

    public boolean waitForLoaderToDisappear()
    {
        return super.waitForElementToVanish(loader,5);
    }

    public boolean insertSearchTerm(String searchTerm)
    {
        return super.insertText(searchField,searchTerm);
    }

    public boolean waitForSearchResults()
    {

        int width = super.getElement(By.className("searchPopUp")).getSize().width;
        int widthBefore = -1;
        while(widthBefore < width )
        {
            widthBefore = width;
            utils.waitAction(1);
            width = super.getElement(By.className("searchPopUp")).getSize().width;
        }

        return width > 0;
    }

    public boolean clickOnSearchResults(int itemNumber)
    {
        WebElement element = super.getElementFromList(searchResultsItem,itemNumber);
        return super.clickOnElement(element);
    }
}
