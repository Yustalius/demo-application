package sdb.app.api.model.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.SneakyThrows;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.model.validation.CreateValidationGroup;
import sdb.app.api.model.validation.LoginValidationGroup;
import sdb.app.api.model.validation.RegistrationValidationGroup;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterJson(
    @NotBlank(message = "Field 'username' required", groups = {RegistrationValidationGroup.class, LoginValidationGroup.class})
    String username,
    @NotBlank(message = "Field 'password' required", groups = {RegistrationValidationGroup.class, LoginValidationGroup.class})
    String password,
    @NotBlank(message = "Field 'name' required", groups = RegistrationValidationGroup.class)
    String firstName,
    @NotBlank(message = "Field 'lastName' required", groups = RegistrationValidationGroup.class)
    String lastName,
    @NotNull(message = "Field 'age' required", groups = RegistrationValidationGroup.class)
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
