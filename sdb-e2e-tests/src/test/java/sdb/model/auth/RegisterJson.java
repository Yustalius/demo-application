package sdb.model.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import sdb.data.entity.auth.RegisterEntity;

public record RegisterJson(
    String username,
    String password,
    String firstName,
    String lastName,
    Integer age
) {
  public static @NonNull RegisterJson fromEntity(RegisterEntity entity) {
    RegisterJson json = new RegisterJson(
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
