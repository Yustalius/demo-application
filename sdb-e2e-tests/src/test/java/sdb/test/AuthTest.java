package sdb.test;

import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.User;
import sdb.model.auth.LoginRequest;
import sdb.model.auth.RegisterDTO;
import sdb.model.auth.Token;
import sdb.model.user.UserDTO;
import sdb.service.AuthClient;
import sdb.service.UserClient;

import static org.assertj.core.api.Assertions.assertThat;
import static sdb.utils.RandomUtils.randomUser;

public class AuthTest {
  private final AuthClient authClient = AuthClient.getInstance();
  private final UserClient userClient = UserClient.getInstance();

  @Test
  void registerUserTest() {
    UserDTO newUser = authClient.createNewUser(randomUser());

    assertThat(userClient.getUser(newUser.id())).isNotNull();
  }

  @Test
  @User
  void loginTest(LoginRequest login) {
    Token token = authClient.login(new LoginRequest(
        login.username(),
        login.password()
    ));

    assertThat(token).isNotNull();
  }
}
