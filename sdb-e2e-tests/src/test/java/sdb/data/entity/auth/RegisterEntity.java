package sdb.data.entity.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import sdb.model.auth.RegisterDTO;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterEntity {
  private Integer id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private Integer age;

  public static @NonNull RegisterEntity fromJson(RegisterDTO json) {
    RegisterEntity entity = new RegisterEntity();
    entity.setUsername(json.username());
    entity.setPassword(json.password());
    entity.setFirstName(json.firstName());
    entity.setLastName(json.lastName());
    entity.setAge(json.age());

    return entity;
  }
}
