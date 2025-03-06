import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sdb.app.controller.UserController;
import sdb.app.model.user.UserDTO;
import sdb.app.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static utils.TestDataGenerator.generateUsers;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private final List<UserDTO> testUsers = generateUsers(3);

  @Test
  void getAllUsersShouldReturnListOfUsers() {
    when(userService.getUsers()).thenReturn(testUsers);

    List<UserDTO> result = userController.getAllUsers();

    assertThat(result).containsExactlyElementsOf(testUsers);
    verify(userService, times(1)).getUsers();
  }

  @Test
  void getUserReturnsUser() {
    when(userService.get(1)).thenReturn(Optional.of(testUsers.get(0)));

    var response = userController.getUser(1);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isEqualTo(testUsers.get(0));
    verify(userService).get(1);
  }

  @Test
  void getUserWhenUserNotFoundReturns404() {
    when(userService.get(1)).thenReturn(Optional.empty());

    var response = userController.getUser(1);

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    verify(userService).get(1);
  }

  @Test
  void deleteUserWhenUserExistsDeletesAndReturns204() {
    when(userService.get(1)).thenReturn(Optional.of(testUsers.get(0)));

    var response = userController.deleteUser(1);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    verify(userService).delete(1);
  }

  @Test
  void deleteUserWhenUserNotFoundReturns404() {
    when(userService.get(1)).thenReturn(Optional.empty());

    var response = userController.deleteUser(1);

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
      verify(userService, never()).delete(anyInt());
  }

  @Test
  @Disabled
  void updateUserWhenUserExistsReturnsUpdatedUser() {
    UserDTO updatedUser = new UserDTO(1, "Jane", "Doe", 31);
    when(userService.get(1)).thenReturn(Optional.of(testUsers.get(0)));
//    when(userService.update(eq(1), any())).thenReturn(updatedUser);

    var response = userController.updateUser(1, updatedUser);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isEqualTo(updatedUser);
    verify(userService).update(1, updatedUser);
  }

  @Test
  void updateUserWhenUserNotFoundReturns404() {
    when(userService.get(1)).thenReturn(Optional.empty());

    var response = userController.updateUser(1, testUsers.get(0));

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    verify(userService, never()).update(anyInt(), any());
  }
}
