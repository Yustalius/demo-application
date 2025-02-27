package sdb.test;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.User;
import sdb.model.user.UserDTO;
import sdb.service.UserClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

  private final UserClient userClient = UserClient.getInstance();
  private final Faker faker = new Faker();

  @Test
  void getAllUsersTest() {
    List<UserDTO> allUsers = userClient.getAllUsers();

    assertThat(allUsers).isNotEmpty();
  }

  @Test
  @User
  void getUserInfoTest(UserDTO user) {
    UserDTO userInfo = userClient.getUser(user.id());

    assertThat(userInfo.id()).isEqualTo(user.id());
  }

  @Test
  @User
  void deleteUserTest(UserDTO user) {
    userClient.deleteUser(user.id());

    assertThat(userClient.getUser(user.id())).isNull();
  }

  @Test
  @User
  void updateUserTest(UserDTO user) {
    UserDTO updateUserRequest = new UserDTO(
        null,
        faker.name().firstName(),
        faker.name().lastName(),
        faker.number().numberBetween(18, 100),
        null
    );

    userClient.updateUser(user.id(), updateUserRequest);

    UserDTO updatedUser = userClient.getUser(user.id());
    assertThat(updatedUser.firstName()).isEqualTo(updateUserRequest.firstName());
    assertThat(updatedUser.lastName()).isEqualTo(updateUserRequest.lastName());
    assertThat((updatedUser.age())).isEqualTo(updateUserRequest.age());
  }
}
