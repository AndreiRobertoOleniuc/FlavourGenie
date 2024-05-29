package ch.webec.recipeapp.e2e.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login {

    @FindBy(css = "#username")
    private WebElement usernameInput;

    @FindBy(css = "#password")
    private WebElement passwordInput;

    @FindBy(css = "body > main > div > div > form > button")
    private WebElement loginButton;

    public Login(WebDriver driver, int port) {
        driver.navigate().to("http://localhost:" + port + "/login");
        PageFactory.initElements(driver, this);
        this.login();
    }

    private void login() {
        usernameInput.sendKeys("basic");
        passwordInput.sendKeys("123456");
        loginButton.click();
    }
}
