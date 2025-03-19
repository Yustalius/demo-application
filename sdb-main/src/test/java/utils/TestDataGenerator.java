package utils;

import net.datafaker.Faker;
import sdb.core.model.user.UserDTO;

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
  public static List<UserDTO> generateUsers(int count) {
    List<UserDTO> users = new ArrayList<>();
    for (int i = 1; i <= count; i++) {
      UserDTO user = new UserDTO(
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
  public static UserDTO generateUser() {
    return new UserDTO(
        faker.number().randomDigitNotZero(), // ID
        faker.name().firstName(),            // firstName
        faker.name().lastName(),             // lastName
        faker.number().numberBetween(18, 99) // age
    );
  }
}