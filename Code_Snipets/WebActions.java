import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class WebActions {

    protected WebDriver driver;
    protected Utils utils;

    public WebActions(WebDriver driver) {
        this.driver = driver;
        utils = new Utils();
    }

    protected boolean navigateToWebPage(String url)
    {
        return this.navigateToWebPage(url,false);
    }

    protected boolean navigateToWebPage(String url,boolean exactMatch) {
        this.driver.get(url);
        String currentUrl = this.driver.getCurrentUrl();
        if(exactMatch)
            return currentUrl.equals(url);

        return currentUrl.contains(url);
    }

    public boolean waitForElementToBeClickable(int timeToWaitInSeconds, By by)
    {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeToWaitInSeconds);
            wait.until(ExpectedConditions.elementToBeClickable(by));
        }
        catch (TimeoutException myException)
        {

            return false;
        }
        catch (Exception e)
        {
            return false;
        }



        return true;

    }

    /**
     * Returns the web element
     *
     * @param by
     * @return the matching web element.
     */
    protected WebElement getElement(By by) {
        try {
            return driver.findElement(by);
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return null;
        }
    }


    /**
     * Returns a list of elements
     *
     * @param by
     * @return a list of web elements
     */
    protected List<WebElement> getElements(By by) {
        try {
            return driver.findElements(by);
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Insert text to the web element
     *
     * @param element
     * @param textToInsert
     * @return true if text inserted correctly.
     */
    protected boolean insertText(WebElement element, String textToInsert, long delayInMillis) {
        if (element != null) {
            if (delayInMillis > 0) {
                for (char c : textToInsert.toCharArray()) {
                    utils.waitActionInMillis(delayInMillis);
                    element.sendKeys("" + c);
                }
            } else
                element.sendKeys(textToInsert);

            String textAfterInsert = this.getTextFromElementValue(element);

            return textToInsert.equals(textAfterInsert);
        }
        return false;
    }

    protected boolean insertText(By by, String textToInsert) {
        WebElement element = this.getElement(by);
        return this.insertText(element, textToInsert, 0);
    }

    /**
     * Returns the element value text (from the 'value' attribute)
     *
     * @param element
     * @return the web element value
     */
    protected String getTextFromElementValue(WebElement element) {
        String text = "";
        if (element != null) {
            try {
                text = element.getText();
                if (text.isEmpty())
                    text = this.getElementAttributeValue(element, "value");
            } catch (StaleElementReferenceException e) {
                System.out.println("Couldn't find text for element");
            }
        }
        return text;
    }

    /**
     * Returns the web element requested attribute value
     *
     * @param element
     * @param attributeName
     * @return the requested attribute value / null
     */
    protected String getElementAttributeValue(WebElement element, String attributeName) {
        if (element == null) {
            return "";
        }

        try {
            String attribute = element.getAttribute(attributeName);
            return attribute != null ? attribute : "";
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            return "";
        }
    }


    public boolean clickOnElement(By by) {
        WebElement element = this.getElement(by);
        if (element == null) {
            //todo: add log for element is null
            return false;
        }

        return this.clickOnElement(element);
    }

    /**
     * Clicks on web element
     *
     * @param element
     * @return true if clicked
     */
    public boolean clickOnElement(WebElement element) {
        if (element != null) {
            try {
                element.click();
                return true;
            } catch (ElementNotInteractableException | StaleElementReferenceException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }



    /**
     * Waits for web element. the web element will not necessarily be displayed. depends on the configuration of checkIfDisplayed
     *
     * @param checkIfDisplayed
     * @param by
     * @param timeoutInSeconds
     * @return true if the web element appeared in the required time
     */
    protected boolean waitForElement(boolean checkIfDisplayed, By by, long timeoutInSeconds) {
        long startTime = utils.currentTimeMillis();

        boolean isFound = false;

        while (!isFound && !utils.isTimeout(startTime, timeoutInSeconds)) {
            WebElement element = this.getElement(by);
            if (checkIfDisplayed)
                isFound = this.isElementDisplayed(element);
            else
                isFound = element != null;
        }

        return isFound;
    }

    /**
     * Waits for web element (the element will not necessarily be displayed)
     *
     * @param by
     * @param timeoutInSeconds
     * @return true if the web element was found in the required time
     */
    protected boolean waitForElement(By by, long timeoutInSeconds) {
        return this.waitForElement(false, by, timeoutInSeconds);
    }


    /**
     * Checks if the web element is displayed
     *
     * @param element
     * @return true if the web element is displayed
     */
    protected boolean isElementDisplayed(WebElement element) {
        if (element == null)
            return false;

        return element.isDisplayed();
    }


    /**
     * Waits for element to vanish
     *
     * @param element
     * @param timeout
     * @return true if the web element has vanished in the required time
     */
    protected boolean waitForElementToVanish(WebElement element, long timeout) {
        long startTime = utils.currentTimeMillis();

        boolean isFound = this.isElementEnabled(element);

        while (isFound && !utils.isTimeout(startTime, timeout)) {
            isFound = this.isElementEnabled(element);
        }

        return !isFound;
    }

    /**
     * Waits for element to appear and then vanish
     *
     * @param by
     * @param timeout
     * @return true if the web element has vanished in the required time
     */
    protected boolean waitForElementToVanish(By by, long timeout) {
        boolean result = this.waitForElement(by, timeout);
        if (result) {
            WebElement element = this.getElement(by);
            return this.waitForElementToVanish(element, timeout);
        }

        return true;
    }


    /**
     * Returns the web element text
     *
     * @param element
     * @return the web element text
     */
    protected String getElementText(WebElement element) {
        if (element != null)
            try {
                return element.getText().trim();
            } catch (StaleElementReferenceException e) {
                System.out.println("Element was stale, returning empty string");
                return "";
            }
        return "";
    }

    /**
     * Returns sub web element by parent web element
     *
     * @param mainElement
     * @param by
     * @return sub web element
     */
    protected WebElement getSubElement(WebElement mainElement, By by) {
        if (mainElement == null)
            return null;
        try {
            return mainElement.findElement(by);
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return null;
        }
    }


    /**
     * Checks if the web element is enabled
     *
     * @param element
     * @return true if the web element id enabled
     */
    protected boolean isElementEnabled(WebElement element) {
        if (element == null)
            return false;

        try {
            return element.isEnabled();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    protected WebElement getElementFromList(By elementLocator, int elementIndex) {
        List<WebElement> elementsList = this.getElements(elementLocator);

        if (elementsList.size() <= elementIndex) {
            return null;
        }
        return elementsList.get(elementIndex);
    }

}
