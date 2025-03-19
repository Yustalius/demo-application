package sdb.core.model.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.SneakyThrows;
import sdb.core.data.entity.auth.RegisterEntity;
import sdb.core.model.validation.LoginValidationGroup;
import sdb.core.model.validation.RegistrationValidationGroup;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterJson(
    @NotBlank(message = "Field 'username' required", groups = {RegistrationValidationGroup.class, LoginValidationGroup.class})
    @Schema(description = "Username пользователя")
    String username,
    @NotBlank(message = "Field 'password' required", groups = {RegistrationValidationGroup.class, LoginValidationGroup.class})
    @Schema(description = "Пароль пользователя")
    String password,
    @NotBlank(message = "Field 'name' required", groups = RegistrationValidationGroup.class)
    @Schema(description = "Имя пользователя")
    String firstName, 
    @NotBlank(message = "Field 'lastName' required", groups = RegistrationValidationGroup.class)
    @Schema(description = "Фамилия пользователя")
    String lastName,
    @NotNull(message = "Field 'age' required", groups = RegistrationValidationGroup.class)
    @Schema(description = "Возраст пользователя")
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
