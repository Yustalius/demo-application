package sdb.app.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.app.data.entity.user.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserEntityRowMapper instance = new UserEntityRowMapper();

  private UserEntityRowMapper() {
  }

  @Override
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntity result = new UserEntity();
    result.setId(rs.getInt("id"));
    result.setFirstName(rs.getString("first_name"));
    result.setLastName(rs.getString("last_name"));
    result.setAge(rs.getInt("age"));

    return result;
  }
}
