package ch.webec.recipeapp.e2e;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT,properties = {"spring.profiles.active=local"})
public class AboutPageIT {
    @LocalServerPort
    int port;

    @Test
    void fhnwLink() {
        var driver = new ChromeDriver();
        driver.navigate().to("http://localhost:%d/".formatted(port));
        var text= "hello";
    }

}
