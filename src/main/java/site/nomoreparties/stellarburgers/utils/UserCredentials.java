package site.nomoreparties.stellarburgers.utils;

import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.models.User;

public class UserCredentials {
    public static final String DOMAIN_POSTFIX = "@yandex.ru";
    public String email;
    public String password;
    public String name;

    public UserCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserCredentials() {
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.email, user.password, user.name);
    }

    public UserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserCredentials setName(String name) {
        this.name = name;
        return this;
    }

    public static UserCredentials getWithRandomEmail(User user) {
        return new UserCredentials().setEmail(RandomStringUtils.randomAlphabetic(5) + DOMAIN_POSTFIX).setPassword(user.password);
    }

    public static UserCredentials getWithRandomPassword(User user) {
        return new UserCredentials().setPassword(RandomStringUtils.randomAlphabetic(10)).setEmail(user.email);
    }

    public static UserCredentials getWithEmailOnly(User user) {
        return new UserCredentials().setEmail(user.email);
    }

    public static UserCredentials getWithPasswordOnly(User user) {
        return new UserCredentials().setPassword(user.password);
    }

    public static UserCredentials changeUserEmailOnly(User user) {
        return new UserCredentials().setEmail(RandomStringUtils.randomAlphabetic(5) + DOMAIN_POSTFIX).setPassword(user.password).setName(user.name);
    }

    public static UserCredentials changeUserPasswordOnly(User user) {
        return new UserCredentials().setPassword(RandomStringUtils.randomAlphabetic(10)).setEmail(user.email).setName(user.name);
    }

    public static UserCredentials changeUserNameOnly(User user) {
        return new UserCredentials().setName(RandomStringUtils.randomAlphabetic(10)).setEmail(user.email).setPassword(user.password);
    }

    public static UserCredentials changeUser(User user) {
        return new UserCredentials().setEmail(RandomStringUtils.randomAlphabetic(5) + DOMAIN_POSTFIX).setPassword(RandomStringUtils.randomAlphabetic(10)).setName(RandomStringUtils.randomAlphabetic(10));
    }
}