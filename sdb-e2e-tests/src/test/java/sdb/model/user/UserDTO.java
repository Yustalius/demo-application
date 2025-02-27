package sdb.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import sdb.data.entity.user.UserEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
    @JsonProperty("id")
    Integer id,
    @JsonProperty("firstName")
    String firstName,
    @JsonProperty("lastName")
    String lastName,
    @JsonProperty("age")
    Integer age
) {
  public static UserDTO fromEntity(@NonNull UserEntity user) {
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
