import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sdb.app.controller.AuthController;
import sdb.app.model.auth.RegisterJson;
import sdb.app.model.user.UserDTO;
import sdb.app.service.AuthService;
import sdb.app.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private AuthService authService;

  @Mock
  private UserService userService;

  @InjectMocks
  private AuthController authController;

  private RegisterJson validRegisterJson;
  private UserDTO registeredUser;
  private RegisterJson loginJson;

  @BeforeEach
  void setUp() {
    validRegisterJson = new RegisterJson("testuser", "password", "John", "Doe", 30);
    registeredUser = new UserDTO(1, "John", "Doe", 30);
    loginJson = new RegisterJson("testuser", "password", "", "", null);
  }

  @Test
  void register_ShouldReturn200_WhenUserCreated() {
    when(authService.register(validRegisterJson)).thenReturn(registeredUser);

    ResponseEntity<UserDTO> response = authController.register(validRegisterJson);

    verify(authService).register(validRegisterJson);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(registeredUser);
  }

  @Test
  void login_ShouldReturn200_WhenUserExists() {
    UserDTO loggedInUser = new UserDTO(1, "John", "Doe", 30);
    when(authService.login(loginJson)).thenReturn(loggedInUser);

    ResponseEntity<UserDTO> response = authController.login(loginJson);

    verify(authService).login(loginJson);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(loggedInUser);
  }
}