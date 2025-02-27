package sdb.model.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import sdb.data.entity.auth.RegisterEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterDTO(
    String username,
    String password,
    String firstName,
    String lastName,
    Integer age
) {
  public RegisterDTO(String username, String password) {
    this(username, password, null, null, null);
  }

  public static @NonNull RegisterDTO fromEntity(RegisterEntity entity) {
    RegisterDTO json = new RegisterDTO(
        entity.getUsername(),
        entity.getPassword(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getAge()
    );

    return json;
  }

  @SneakyThrows
  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
