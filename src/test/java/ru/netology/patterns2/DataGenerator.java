package ru.netology.patterns2;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RedirectSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    @Value
    public static class RegistrationData {
        String login;
        String password;
        String status;
    }

    private static RegistrationData sendAuthRequest(RegistrationData user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        return user;
    }

    public static String getLogin() {
        return faker.name().username();
    }

    public static String getPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationData getUser(String status) {
            return new RegistrationData(getLogin(), getPassword(), status);
        }

        public static RegistrationData getRegisteredUser(String status) {
            return sendAuthRequest(getUser(status));
        }
    }
}