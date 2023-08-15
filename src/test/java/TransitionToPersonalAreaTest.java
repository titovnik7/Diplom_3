import api.UserClient;
import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.ValidatableResponse;
import model.HomePage;
import model.LoginPage;
import model.PersonalAreaPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransitionToPersonalAreaTest {
    private WebDriver driver;
    private UserClient userClient;
    private Faker faker;
    private String userEmail;
    private String userPassword;
    private LoginPage loginPage;

    @Before
    public void setUp() {
        faker = new Faker();
        String userName = (faker.name().firstName() + faker.name().lastName());
        userEmail = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        userPassword = RandomStringUtils.randomAlphanumeric(6);

        userClient = new UserClient();
        userClient.create(userEmail, userPassword, userName);
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
    }

    @After
    public void cleanUp() {
        ValidatableResponse login = userClient.login(userEmail, userPassword);
        String bearerToken = login.extract().path("accessToken");
        userClient.delete(bearerToken);
        driver.quit();
    }

    @Test
    public void checkTransitionToPersonalAreaTest() {
        driver.manage().window().maximize();
        driver.get(HomePage.HOME_PAGE_URL);
        driver.findElement(By.xpath(HomePage.XPATH_LOGIN_HOME_PAGE_BUTTON)).click();
        loginPage.inputLoginDataAndPressButton(driver, userEmail, userPassword);
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(HomePage.XPATH_PERSONAL_AREA_BUTTON)));
        driver.findElement(By.xpath(HomePage.XPATH_PERSONAL_AREA_BUTTON)).click();
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(PersonalAreaPage.getXpathAccountText()));
        Assert.assertEquals(PersonalAreaPage.getPersonalAreaText(), driver.findElement(PersonalAreaPage.getXpathAccountText()).getText());
    }
}
