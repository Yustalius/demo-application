package sdb.core.data.entity.auth;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import sdb.core.model.auth.RegisterJson;

@Getter
@Setter
@ToString
public class RegisterEntity {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private Integer age;

  public static @NonNull RegisterEntity fromJson(RegisterJson json) {
    RegisterEntity entity = new RegisterEntity();
    entity.setUsername(json.username());
    entity.setPassword(json.password());
    entity.setFirstName(json.firstName());
    entity.setLastName(json.lastName());
    entity.setAge(json.age());

    return entity;
  }
}
