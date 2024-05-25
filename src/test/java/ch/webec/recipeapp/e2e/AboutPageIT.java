package ch.webec.recipeapp.e2e;

import ch.webec.recipeapp.e2e.utils.Login;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,properties = {"spring.profiles.active=local"})
public class AboutPageIT {
    @LocalServerPort
    int port;

    WebDriver driver = new ChromeDriver();

    @Test
    void visitAboutPage() {
        new Login(driver, port);
    }

}
