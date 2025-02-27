package sdb.utils;

import jakarta.annotation.Nonnull;
import net.datafaker.Faker;
import sdb.model.auth.RegisterDTO;

public class RandomUtils {

  private static final Faker faker = new Faker();

  @Nonnull
  public static String randomUsername() {
    return faker.name().username();
  }

  @Nonnull
  public static String randomPassword() {
    return faker.internet().password();
  }

  @Nonnull
  public static String randomName() {
    return faker.name().firstName();
  }

  @Nonnull
  public static String randomLastName() {
    return faker.name().lastName();
  }

  public static int randomAge() {
    return faker.number().numberBetween(18, 100);
  }

  @Nonnull
  public static RegisterDTO randomUser() {
    return new RegisterDTO(
        randomUsername(),
        randomPassword(),
        randomName(),
        randomLastName(),
        randomAge()
    );
  }
}
