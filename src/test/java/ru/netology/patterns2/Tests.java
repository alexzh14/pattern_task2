package ru.netology.patterns2;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.patterns2.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.patterns2.DataGenerator.Registration.getUser;
import static ru.netology.patterns2.DataGenerator.getPassword;

public class Tests {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldLogWithActiveRegUser () {
        var registredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registredUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    public void shouldLogWithBlockedRegUser () {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldLogWithNotRegUser () {
        var notRegUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldLogWithWrongPass () {
        var registredUser = getRegisteredUser("active");
        var wrongPassword = getPassword();
        $("[data-test-id='login'] input").setValue(registredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }
}
