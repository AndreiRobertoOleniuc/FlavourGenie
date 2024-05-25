package ch.webec.recipeapp.e2e;

import ch.webec.recipeapp.e2e.pages.AboutPage;
import ch.webec.recipeapp.e2e.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,properties = {"spring.profiles.active=local"})
public class AboutPageIT {
    @LocalServerPort
    int port;

    WebDriver driver = new ChromeDriver();

    @Test
    void visitAboutPage() {
        var loginPage = new AboutPage(driver, port);
    }

}
