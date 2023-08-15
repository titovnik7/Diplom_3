package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    public static final String HOME_PAGE_URL = "https://stellarburgers.nomoreparties.site/";
    public static final By LOGIN_HOME_PAGE_BUTTON = By.xpath(".//button[text() = 'Войти в аккаунт']");
    public static final String XPATH_LOGIN_HOME_PAGE_BUTTON = ".//button[text() = 'Войти в аккаунт']";
    public static final String XPATH_PERSONAL_AREA_BUTTON = ".//nav/a/p";
    public static final By XPATH_CHECKOUT_BUTTON = By.xpath(".//button[text() = 'Оформить заказ']");
    public static final By XPATH_TEXT_HOME_PAGE = By.xpath(".//h1[text()= 'Соберите бургер']");
    public static final By XPATH_TAB_BUNS = By.xpath(".//span[text()='Булки']");
    public static final By XPATH_TAB_SAUCES = By.xpath(".//span[text()='Соусы']");
    public static final By XPATH_TAB_FILLINGS = By.xpath(".//span[text() = 'Начинки']");
    public static final By XPATH_SECTION_BUNS = By.xpath(".//h2[text()='Булки']");
    public static final By XPATH_SECTION_SAUCES = By.xpath(".//h2[text()='Соусы']");
    public static final By XPATH_SECTION_FILLINGS = By.xpath(".//h2[text()='Начинки']");
    public static final By ACTIVE_TAB_BUNS = By.xpath(".//div[@class = 'tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect']/span[text() = 'Булки']");
    public static final By ACTIVE_TAB_SAUCE = By.xpath(".//div[@class = 'tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect']/span[text() = 'Соусы']");
    public static final By ACTIVE_TAB_FILLINGS = By.xpath(".//div[@class = 'tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect']/span[text() = 'Начинки']");
    private static WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public static By getXpathCheckoutButtonText(WebDriver driver) {
        return XPATH_CHECKOUT_BUTTON;
    }

    public static void clickLogin(WebDriver driver) {
        driver.findElement(LOGIN_HOME_PAGE_BUTTON).click();
    }
}
