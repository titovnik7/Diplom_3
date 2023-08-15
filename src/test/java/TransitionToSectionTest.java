import io.github.bonigarcia.wdm.WebDriverManager;
import model.HomePage;
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

@RunWith(Parameterized.class)
public class TransitionToSectionTest {
    private final By sectionConstructorTab;
    private final By neighboringSection;
    private final String expectedName;
    private final By activeTab;
    private WebDriver driver;


    public TransitionToSectionTest(By sectionConstructorTab, String expectedName, By neighboringSection, By activeTab) {
        this.sectionConstructorTab = sectionConstructorTab;
        this.neighboringSection = neighboringSection;
        this.expectedName = expectedName;
        this.activeTab = activeTab;
    }


    @Parameterized.Parameters
    public static Object[][] getTestParameters() {
        return new Object[][]{
                {HomePage.XPATH_TAB_BUNS, "Булки", HomePage.XPATH_TAB_SAUCES, HomePage.ACTIVE_TAB_BUNS},
                {HomePage.XPATH_TAB_SAUCES, "Соусы", HomePage.XPATH_TAB_FILLINGS, HomePage.ACTIVE_TAB_SAUCE},
                {HomePage.XPATH_TAB_FILLINGS, "Начинки", HomePage.XPATH_TAB_SAUCES, HomePage.ACTIVE_TAB_FILLINGS}
        };
    }

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

    @After
    public void cleanUp() {
        driver.quit();
    }

    @Test
    public void checkTransitionToSectionTest() {
        driver.manage().window().maximize();
        driver.get(HomePage.HOME_PAGE_URL);
        driver.findElement(neighboringSection).click();
        driver.findElement(sectionConstructorTab).click();
        String actualName = driver.findElement(activeTab).getText();
        Assert.assertEquals(expectedName, actualName);
    }
}
