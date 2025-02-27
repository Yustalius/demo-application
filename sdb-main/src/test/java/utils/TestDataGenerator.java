package utils;

import net.datafaker.Faker;
import sdb.app.api.model.user.UserJson;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {

  private static final Faker faker = new Faker();

  /**
   * Генерирует список тестовых пользователей.
   *
   * @param count количество пользователей для генерации
   * @return список сгенерированных пользователей
   */
  public static List<UserJson> generateUsers(int count) {
    List<UserJson> users = new ArrayList<>();
    for (int i = 1; i <= count; i++) {
      UserJson user = new UserJson(
          i, // ID
          faker.name().firstName(), // firstName
          faker.name().lastName(),  // lastName
          faker.number().numberBetween(18, 99) // age
      );
      users.add(user);
    }
    return users;
  }

  /**
   * Генерирует одного тестового пользователя.
   *
   * @return сгенерированный пользователь
   */
  public static UserJson generateUser() {
    return new UserJson(
        faker.number().randomDigitNotZero(), // ID
        faker.name().firstName(),            // firstName
        faker.name().lastName(),             // lastName
        faker.number().numberBetween(18, 99) // age
    );
  }
}