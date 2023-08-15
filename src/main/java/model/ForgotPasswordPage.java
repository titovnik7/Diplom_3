package model;

import org.openqa.selenium.WebDriver;

public class ForgotPasswordPage {
    private final static String RECOVERY_FORM_PAGE = "https://stellarburgers.nomoreparties.site/forgot-password";
    private final static String XPATH_LOGIN_FROM_FORGOT_PASSWORD_PAGE_BUTTON = ".//a[@class = 'Auth_link__1fOlj']";
    private WebDriver driver;

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public static String getLoginFromForgotPasswordPage() {
        return RECOVERY_FORM_PAGE;
    }

    public static String getXpathLoginFromForgotPasswordPageButton() {
        return XPATH_LOGIN_FROM_FORGOT_PASSWORD_PAGE_BUTTON;
    }
}
