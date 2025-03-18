package sdb.data.entity.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import sdb.model.user.UserDTO;

@Getter
@Setter
@ToString
public class UsersEntity {
  private Integer id;
  private String firstName;
  private String lastName;
  private Integer age;

  public static @NonNull UsersEntity fromJson(UserDTO json) {
    UsersEntity user = new UsersEntity();
    user.setId(json.id());
    user.setFirstName(json.firstName());
    user.setLastName(json.lastName());
    user.setAge(json.age());

    return user;
  }
}
