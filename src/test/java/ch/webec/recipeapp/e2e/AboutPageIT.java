package ch.webec.recipeapp.e2e;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,properties = {"spring.profiles.active=local"})
public class AboutPageIT {
    @LocalServerPort
    int port;

    WebDriver driver = new ChromeDriver();

    @Test
    void fhnwLink() {
        driver.navigate().to("http://localhost:%d/".formatted(port));
        var text= "hello";
    }

}
