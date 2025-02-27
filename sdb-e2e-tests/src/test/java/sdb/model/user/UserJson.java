package sdb.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
    @JsonProperty("id")
    Integer id,
    @JsonProperty("firstName")
    String firstName,
    @JsonProperty("lastName")
    String lastName,
    @JsonProperty("age")
    Integer age
) {
/*  public static UserJson fromEntity(UserEntity user) {
    return new UserJson(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getAge()
    );
  }*/

  @SneakyThrows
  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
