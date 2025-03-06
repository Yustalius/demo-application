package sdb.app.data.entity.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import sdb.app.model.user.UserDTO;

@Getter
@Setter
@ToString
public class UserEntityOld {
  private Integer id;
  private String firstName;
  private String lastName;
  private Integer age;

  public static @NonNull UserEntityOld fromJson(UserDTO json) {
    UserEntityOld user = new UserEntityOld();
    user.setId(json.id());
    user.setFirstName(json.firstName());
    user.setLastName(json.lastName());
    user.setAge(json.age());

    return user;
  }
}
