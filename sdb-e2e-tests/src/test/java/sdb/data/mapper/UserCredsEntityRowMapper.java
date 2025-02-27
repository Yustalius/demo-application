package sdb.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.data.entity.auth.RegisterEntity;
import sdb.data.entity.user.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCredsEntityRowMapper implements RowMapper<RegisterEntity> {

  public static final UserCredsEntityRowMapper instance = new UserCredsEntityRowMapper();

  private UserCredsEntityRowMapper() {
  }

  @Override
  public RegisterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    RegisterEntity result = new RegisterEntity();
    result.setId(rs.getInt("id"));
    result.setUsername(rs.getString("username"));
    result.setPassword(rs.getString("pass"));

    return result;
  }
}
