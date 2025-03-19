package sdb.core.model.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import sdb.core.model.validation.LoginValidationGroup;
import sdb.core.model.validation.RegistrationValidationGroup;

public record LoginDTO(
    @NotBlank(message = "Field 'username' required")
    @Schema(description = "Username пользователя")
    String username,
    @NotBlank(message = "Field 'password' required")
    @Schema(description = "Пароль пользователя")
    String password
) {

  @SneakyThrows
  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
