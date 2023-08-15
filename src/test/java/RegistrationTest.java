import api.UserClient;
import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.ValidatableResponse;
import model.HomePage;
import model.LoginPage;
import model.RegistrationPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.hamcrest.CoreMatchers.containsString;

public class RegistrationTest {
    private WebDriver driver;
    private RegistrationPage registrationPage;
    private UserClient userClient;
    private String userName;
    private String userEmail;
    private String userPassword;


    @Before
    public void setUp() {
        Faker faker = new Faker();
        userName = faker.name().firstName() + faker.name().lastName();
        userEmail = RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru";
        userPassword = faker.toString();
        userClient = new UserClient();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        registrationPage = new RegistrationPage(driver);
    }

    @After
    public void cleanUp() {
        driver.quit();
        ValidatableResponse login = userClient.login(userEmail, userPassword);
        String clientBearerToken = login.extract().path("accessToken");
        try {
            clientBearerToken = clientBearerToken.replace("Bearer ", "");
            userClient.delete(clientBearerToken);
        } catch (NullPointerException ignore) {
        }
    }

    @Test
    public void checkRegistrationSuccessTest() {

        LoginPage loginPage = new LoginPage(driver);

        driver.get(HomePage.HOME_PAGE_URL);
        HomePage.clickLogin(driver);
        loginPage.clickRegistration(driver);
        registrationPage.inputRegistrationDataAndPressButton(driver, userName, userEmail, userPassword);
        loginPage.waitLoadInputButton();
        MatcherAssert.assertThat(loginPage.getLoginTextButton(driver), containsString("Войти"));

    }

    @Test
    public void checkRegistrationWithIncorrectPassword() {
        driver.manage().window().maximize();
        LoginPage loginPage = new LoginPage(driver);

        userPassword = RandomStringUtils.randomAlphanumeric(5);

        driver.get(HomePage.HOME_PAGE_URL);
        HomePage.clickLogin(driver);
        loginPage.clickRegistration(driver);
        registrationPage.inputRegistrationDataAndPressButton(driver, userName, userEmail, userPassword);
        registrationPage.waitLoadSmallTextError();
        MatcherAssert.assertThat(registrationPage.getSmallPasswordErrorText(driver), containsString("Некорректный пароль"));
    }
}
