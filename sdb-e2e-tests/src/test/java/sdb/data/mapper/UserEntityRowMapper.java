package sdb.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.data.entity.user.UsersEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntityRowMapper implements RowMapper<UsersEntity> {

  public static final UserEntityRowMapper instance = new UserEntityRowMapper();

  private UserEntityRowMapper() {
  }

  @Override
  public UsersEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UsersEntity result = new UsersEntity();
    result.setId(rs.getInt("id"));
    result.setFirstName(rs.getString("first_name"));
    result.setLastName(rs.getString("last_name"));
    result.setAge(rs.getInt("age"));

    return result;
  }
}
