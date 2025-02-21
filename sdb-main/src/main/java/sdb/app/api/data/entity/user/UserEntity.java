package sdb.app.api.data.entity.user;

import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.model.user.UserJson;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserEntity {
  private Integer id;
  private String firstName;
  private String lastName;
  private Integer age;

  public static @NonNull UserEntity fromJson(UserJson json) {
    UserEntity user = new UserEntity();
    user.setId(json.id());
    user.setFirstName(json.firstName());
    user.setLastName(json.lastName());
    user.setAge(json.age());

    return user;
  }
}
