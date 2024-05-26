package ch.webec.recipeapp.e2e;

import ch.webec.recipeapp.e2e.components.Navbar;
import ch.webec.recipeapp.e2e.pages.CreatePage;
import ch.webec.recipeapp.e2e.pages.RecipeDetailsPage;
import ch.webec.recipeapp.e2e.utils.Login;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.profiles.active=local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserFlowsIT {
    @LocalServerPort
    int port;
    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void createRecipe() {
        String recipeId = generateRecipe();
        assertEquals("http://localhost:" + port + "/recipe/" + recipeId, driver.getCurrentUrl());
    }

    @Test
    @Order(2)
    void testFeedback() {
        RecipeDetailsPage recipeDetails = new RecipeDetailsPage(driver);
        recipeDetails.giveRating(4);
        int rating = recipeDetails.getRating();
        assertEquals(4, rating);
    }

    @Test
    @Order(3)
    void testDeleteRecipe() {
        RecipeDetailsPage recipeDetails = new RecipeDetailsPage(driver);
        recipeDetails.deleteRecipe();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlMatches("http://localhost:" + port + "/recipe"));
        assertTrue(driver.getCurrentUrl().endsWith("/recipe"));
    }


    private String generateRecipe() {
        new Login(driver, port);
        Navbar navbar = new Navbar(driver);
        CreatePage createPage = new CreatePage(driver);
        RecipeDetailsPage recipeDetails = new RecipeDetailsPage(driver);
        navbar.clickCreateButton();

        createPage.insertIngredient("chocolate");
        createPage.insertIngredient("flour");
        createPage.insertIngredient("sugar");
        createPage.insertIngredient("eggs");
        createPage.insertIngredient("milk");
        createPage.insertIngredient("sDFsdfwadw");

        createPage.deleteIngredient(5);
        createPage.clickGenerateButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));
        wait.until(ExpectedConditions.urlMatches("http://localhost:" + port + "/recipe/\\d+"));
        return recipeDetails.getCurrentRecipeId();
    }
}
