package ch.webec.recipeapp.e2e.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CreatePage {

    @FindBy(css = "#ingredient-input")
    private WebElement insertIngredient;

    @FindBy(css = "body > main > main > form > div > button")
    private WebElement generateButton;

    @FindBy(css = "#chips-container  div  span")
    private List<WebElement> deleteIngredient;

    public CreatePage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void insertIngredient(String ingredient){
        insertIngredient.sendKeys(ingredient);
        insertIngredient.sendKeys(Keys.RETURN);
    }

    public void clickGenerateButton(){
        generateButton.click();
    }

    public void deleteIngredient(int index){
        deleteIngredient.get(index).click();
    }
}
