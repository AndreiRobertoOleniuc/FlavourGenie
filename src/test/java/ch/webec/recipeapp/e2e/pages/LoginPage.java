package ch.webec.recipeapp.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(css = ".gsi-material-button")
    private WebElement loginButton;

    public LoginPage(WebDriver driver, int port){
        driver.navigate().to("http://localhost:" + port + "/login");
        PageFactory.initElements(driver, this);
    }

    public void clickLoginButton(){
        loginButton.click();
    }
}
