package sdb.core.model.user;

import sdb.core.data.entity.user.UsersEntity;
import sdb.core.model.validation.CreateValidationGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;

public record UserDTO(
    @JsonProperty("id")
    Integer id,
    @JsonProperty("firstName")
    @NotBlank(message = "Field 'name' required", groups = CreateValidationGroup.class)
    String firstName,
    @JsonProperty("lastName")
    @NotBlank(message = "Field 'lastName' required", groups = CreateValidationGroup.class)
    String lastName,
    @JsonProperty("age")
    @NotNull(message = "Field 'age' required", groups = CreateValidationGroup.class)
    Integer age
) {
  public static UserDTO fromEntity(UsersEntity user) {
    return new UserDTO(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getAge()
    );
  }

  @SneakyThrows
  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
