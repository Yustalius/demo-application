package sdb.data.entity.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import sdb.model.user.UserDTO;

@Getter
@Setter
@ToString
public class UserEntity {
  private Integer id;
  private String firstName;
  private String lastName;
  private Integer age;

  public static @NonNull UserEntity fromJson(UserDTO json) {
    UserEntity user = new UserEntity();
    user.setId(json.id());
    user.setFirstName(json.firstName());
    user.setLastName(json.lastName());
    user.setAge(json.age());

    return user;
  }
}
