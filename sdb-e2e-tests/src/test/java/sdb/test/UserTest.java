package sdb.test;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import sdb.model.user.UserJson;
import sdb.service.UserClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

  private final UserClient userClient = UserClient.getInstance();
  private final Faker faker = new Faker();

  @Test
  void getAllUsersTest() {
    List<UserJson> allUsers = userClient.getAllUsers();

    assertThat(allUsers).isNotEmpty();
  }

  @Test
  void getUserInfoTest() {
    List<UserJson> allUsers = userClient.getAllUsers();
    UserJson randomUser = allUsers.stream()
        .findAny()
        .orElseThrow();

    UserJson user = userClient.getUser(randomUser.id());

    assertThat(user.id()).isEqualTo(randomUser.id());
  }

  @Test
  void deleteUserTest() {
    List<UserJson> allUsers = userClient.getAllUsers();
    UserJson randomUser = allUsers.stream()
        .findAny()
        .orElseThrow();

    userClient.deleteUser(randomUser.id());

    assertThat(userClient.getUser(randomUser.id())).isNull();
  }

  @Test
  void updateUserTest() {
    List<UserJson> allUsers = userClient.getAllUsers();
    UserJson randomUser = allUsers.stream()
        .findAny()
        .orElseThrow();
    UserJson updateUserRequest = new UserJson(
        null,
        faker.name().firstName(),
        faker.name().lastName(),
        30
    );

    userClient.updateUser(randomUser.id(), updateUserRequest);

    UserJson updatedUser = userClient.getUser(randomUser.id());
    assertThat(updatedUser.firstName()).isEqualTo(updateUserRequest.firstName());
    assertThat(updatedUser.lastName()).isEqualTo(updateUserRequest.lastName());
    assertThat((updatedUser.age())).isEqualTo(updateUserRequest.age());
  }
}
