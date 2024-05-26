package ch.webec.recipeapp.e2e;

import ch.webec.recipeapp.e2e.components.Navbar;
import ch.webec.recipeapp.e2e.pages.CreatePage;
import ch.webec.recipeapp.e2e.utils.Login;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.profiles.active=local"})
public class CreateRecipeIT {
    @LocalServerPort
    int port;

    WebDriver driver = new ChromeDriver();

    @Test
    void createRecipe() {
        new Login(driver, port);
        Navbar navbar = new Navbar(driver);
        CreatePage createPage = new CreatePage(driver);
        navbar.clickCreateButton();

        createPage.insertIngredient("chocolate");
        createPage.insertIngredient("flour");
        createPage.insertIngredient("sugar");
        createPage.insertIngredient("eggs");
        createPage.insertIngredient("milk");
        createPage.insertIngredient("sDF");

        createPage.deleteIngredient(5);
        createPage.clickGenerateButton();

        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:" + port + "/recipe/"));
        driver.quit();
    }
}
