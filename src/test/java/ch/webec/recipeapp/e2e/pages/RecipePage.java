package ch.webec.recipeapp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class RecipePage {
    @FindBy(css = "body main article section.recipes ul li a")
    private List<WebElement> recipes;

    public RecipePage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void clickRecipe(int index){
        recipes.get(index).click();
    }
}
