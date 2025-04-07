package sdb.utils;

import jakarta.annotation.Nonnull;
import net.datafaker.Faker;
import sdb.model.auth.RegisterDTO;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductWhDTO;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

  private static final Faker faker = new Faker();

  public static int randomNumber(int length) {
    if (length < 1 || length > 9) {
      throw new IllegalArgumentException("Длина должна быть от 1 до 9");
    }

    int min = (int) Math.pow(10, length - 1);
    int max = (int) Math.pow(10, length) - 1;

    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  @Nonnull
  public static String randomUsername() {
    String username = faker.name().username();
    String suffix = faker.number().digits(4);
    String prefix = faker.letterify("??");
    return prefix + "_" + username + "_" + suffix;
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

  @Nonnull
  public static ProductWhDTO randomWhProduct() {
    return new ProductWhDTO(
        randomNumber(9),
        UUID.randomUUID().toString(),
        randomNumber(3)
    );
  }

  @Nonnull
  public static ProductCoreDTO randomCoreProduct() {
    return new ProductCoreDTO(
        UUID.randomUUID().toString(),
        null,
        randomNumber(3)
    );
  }
}
