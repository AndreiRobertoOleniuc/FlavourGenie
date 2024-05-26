package ch.webec.recipeapp.e2e.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RecipeDetailsPage {
    @FindBy(css = "#\\32  > section.feedback_createdBy > div.rating-touched > form:nth-child(3) > button > span\n")
    private WebElement generateButton;
}
