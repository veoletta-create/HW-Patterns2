package ru.netology.ibank.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.ibank.data.AuthDataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginSuccessfullyIfActiveUser() {
        var registeredUser = AuthDataGenerator.Registration.getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button[data-test-id='action-login']").click();

        $("h2").shouldHave(text("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = AuthDataGenerator.Registration.getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button[data-test-id='action-login']").click();

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован"))
                .shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = AuthDataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = AuthDataGenerator.getRandomLogin();

        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button[data-test-id='action-login']").click();

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = AuthDataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = AuthDataGenerator.getRandomPassword();

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button[data-test-id='action-login']").click();

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }
}