package sdb.app.api.model.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.model.validation.CreateValidationGroup;

public record RegisterJson(
    String username,
    String password,
    @NotBlank(message = "Field 'name' required", groups = CreateValidationGroup.class)
    String firstName,
    @NotBlank(message = "Field 'lastName' required", groups = CreateValidationGroup.class)
    String lastName,
    @NotNull(message = "Field 'age' required", groups = CreateValidationGroup.class)
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
}
