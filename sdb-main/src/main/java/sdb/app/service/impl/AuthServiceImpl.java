package sdb.app.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.app.data.entity.user.UserCredsEntity;
import sdb.app.data.entity.user.UsersEntity;
import sdb.app.data.repository.UserCredsRepository;
import sdb.app.data.repository.UsersRepository;
import sdb.app.ex.UserNotFoundException;
import sdb.app.model.auth.RegisterJson;
import sdb.app.model.user.UserDTO;
import sdb.app.service.AuthService;
import sdb.app.logging.Logger;

import static sdb.app.data.Databases.transaction;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final Logger logger;
  private final UserCredsRepository userCredsRepository;

  @Override
  @Transactional
  public UserDTO register(@Nonnull RegisterJson json) {
    logger.info("Starting register process for user " + json);
    UserCredsEntity creds = new UserCredsEntity();
    creds.setUsername(json.username());
    creds.setPassword(json.password());

    UsersEntity user = new UsersEntity();
    user.setFirstName(json.firstName());
    user.setLastName(json.lastName());
    user.setAge(json.age());

    user.setUserCreds(creds);
    creds.setUser(user);
    UserCredsEntity createdUser = userCredsRepository.save(creds);
    return new UserDTO(
        createdUser.getId(),
        json.firstName(),
        json.lastName(),
        json.age());
  }

  @Override
  @Transactional(readOnly = true)
  public String login(@Nonnull RegisterJson json) {
    UserCredsEntity creds = userCredsRepository.findByUsername(json.username())
        .orElseThrow(() ->
            new UserNotFoundException("Not found user with username = " + json.username()));

    if (!creds.getPassword().equals(json.password())) {
      throw new RuntimeException("bad creds");
    }

    return generateToken();
  }

  private String generateToken() {
    return "token";
  }
}
