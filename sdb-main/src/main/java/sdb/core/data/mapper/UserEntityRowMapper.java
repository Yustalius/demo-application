package sdb.core.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.core.data.entity.user.UserEntityOld;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntityRowMapper implements RowMapper<UserEntityOld> {

  public static final UserEntityRowMapper instance = new UserEntityRowMapper();

  private UserEntityRowMapper() {
  }

  @Override
  public UserEntityOld mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntityOld result = new UserEntityOld();
    result.setId(rs.getInt("id"));
    result.setFirstName(rs.getString("first_name"));
    result.setLastName(rs.getString("last_name"));
    result.setAge(rs.getInt("age"));

    return result;
  }
}
