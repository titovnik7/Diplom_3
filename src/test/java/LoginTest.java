import api.UserClient;
import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.ValidatableResponse;
import model.ForgotPasswordPage;
import model.HomePage;
import model.LoginPage;
import model.RegistrationPage;
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
public class LoginTest {
    private WebDriver driver;
    private UserClient userClient;
    private Faker faker;
    private String userEmail;
    private String userPassword;
    private final String buttonXpath;
    private final String page;
    private LoginPage loginPage;


    public LoginTest(String buttonXpath, String page) {
        this.buttonXpath = buttonXpath;
        this.page = page;
    }
    @Parameterized.Parameters
    public static Object[][] getTestParameters(){
        return new Object[][]{
                {HomePage.getXpathLoginHomePageButton(),HomePage.getHomePageUrl()},
                {HomePage.getXpathPersonalAreaButton(),HomePage.getHomePageUrl()},
                {RegistrationPage.getXpathLoginFromRegistrationPageButton(), RegistrationPage.getLoginFromRegistrationPage()},
                {ForgotPasswordPage.getXpathLoginFromForgotPasswordPageButton(), ForgotPasswordPage.getLoginFromForgotPasswordPage()}
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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriverManager.chromedriver().setup();
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
    public void checkLoginParamTest(){
        driver.manage().window().maximize();
        driver.get(page);
        driver.findElement(By.xpath(buttonXpath)).click();
        loginPage.inputLoginDataAndPressButton(driver,userEmail,userPassword);
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(HomePage.getXpathCheckoutButtonText(driver)));
        Assert.assertEquals("Оформить заказ", driver.findElement(HomePage.getXpathCheckoutButtonText(driver)).getText());
    }
}
