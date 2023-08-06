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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@RunWith(Parameterized.class)
public class TransitionToConstructorTest {
    private WebDriver driver;
    private UserClient userClient;
    private Faker faker;
    private String userEmail;
    private String userPassword;
    private LoginPage loginPage;
    private final String buttonXpath;


    public TransitionToConstructorTest(String buttonXpath) {
        this.buttonXpath = buttonXpath;
    }
    @Parameterized.Parameters
    public static Object[][] getTestParameters(){
        return new Object[][]{
                {PersonalAreaPage.getXpathConstructorButton()},
                {PersonalAreaPage.getXpathConstructorImage()}
        };
    }
    @Before
    public void setUp(){
        faker = new Faker();
        String userName = (faker.name().firstName() + faker.name().lastName());
        userEmail = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        userPassword = RandomStringUtils.randomAlphanumeric(6);

        userClient = new UserClient();
        userClient.create(userEmail,userPassword, userName);
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
    public void checkParamSwitchToConstructorTest() {
        driver.manage().window().maximize();
        driver.get(LoginPage.openAuthorizationPage());
        loginPage.inputLoginDataAndPressButton(driver,userEmail,userPassword);
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(HomePage.getXpathCheckoutButtonText(driver)));
        driver.findElement(By.xpath(HomePage.getXpathPersonalAreaButton())).click();
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(PersonalAreaPage.getXpathAccountText()));
        driver.findElement(By.xpath(buttonXpath)).click();
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(HomePage.getXpathTextHomePage()));
        Assert.assertEquals("Соберите бургер",driver.findElement(HomePage.getXpathTextHomePage()).getText());
    }
}
