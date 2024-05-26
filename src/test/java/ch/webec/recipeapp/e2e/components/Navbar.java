package ch.webec.recipeapp.e2e.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Navbar {
    @FindBy(css = "body > header > nav > ul > li:nth-child(1) > a")
    private WebElement createButton;

    public Navbar(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void clickCreateButton(){
        createButton.click();
    }
}
