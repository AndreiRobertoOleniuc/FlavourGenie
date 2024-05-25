package ch.webec.recipeapp.e2e.pages;

import org.openqa.selenium.WebDriver;

public class AboutPage {

    public AboutPage(WebDriver driver, int port) {
        driver.navigate().to("http://localhost:" + port + "/");
    }
}
