package sdb.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.SneakyThrows;
import sdb.data.entity.user.UsersEntity;
import sdb.model.TestData;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
    @JsonProperty("id")
    Integer id,
    @JsonProperty("firstName")
    String firstName,
    @JsonProperty("lastName")
    String lastName,
    @JsonProperty("age")
    Integer age,
    @JsonIgnore TestData testData
) {
  public static UserDTO fromEntity(@NonNull UsersEntity user) {
    return new UserDTO(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getAge(),
        null
    );
  }

  public @Nonnull UserDTO addTestData(@Nonnull TestData testData) {
    return new UserDTO(
        id, firstName, lastName, age, testData
    );
  }

  @SneakyThrows
  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
