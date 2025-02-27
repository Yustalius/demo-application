package sdb;

import org.junit.jupiter.api.Test;
import sdb.model.user.UserJson;
import sdb.service.UserClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

  private final UserClient userClient = UserClient.getInstance();

  @Test
  void getAllUsersTest() {
    List<UserJson> allUsers = userClient.getAllUsers();

    assertThat(allUsers).isNotEmpty();
  }
}
