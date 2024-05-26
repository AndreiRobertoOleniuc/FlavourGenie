package ch.webec.recipeapp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RecipeDetailsPage {
    @FindBy(css = ".rating-button")
    private List<WebElement> feedbackBtns;
    @FindBy(css = "#deleteRecipe")
    private WebElement deleteRecipeBtn;

    private WebDriver driver;

    public RecipeDetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public String getCurrentRecipeId() {
        String currentUrl = driver.getCurrentUrl();
        String[] urlParts = currentUrl.split("/");
        return urlParts[urlParts.length - 1];
    }

    public void giveRating(int stars) {
        WebElement feedbackElement = driver.findElement(By.cssSelector(".feedback_createdBy"));
        String initialRating = feedbackElement.getAttribute("data-rating");

        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Stars must be between 1 and 5");
        }
        feedbackBtns.get(stars - 1).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(feedbackElement, "data-rating", initialRating)));
    }

    public int getRating() {
        int starCount = 0;
        for (WebElement button : feedbackBtns) {
            WebElement icon = button.findElement(By.cssSelector(".material-icons-outlined"));
            if ("star".equals(icon.getText())) {
                starCount++;
            }
        }
        return starCount;
    }

    public void deleteRecipe() {
        Actions actions = new Actions(driver);
        WebElement tooltipContainer = driver.findElement(By.cssSelector(".tooltip-container"));
        actions.moveToElement(tooltipContainer).perform();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(deleteRecipeBtn));
        deleteRecipeBtn.click();
    }
}
