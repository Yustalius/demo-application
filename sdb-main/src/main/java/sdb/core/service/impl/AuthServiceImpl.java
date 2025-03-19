package sdb.core.service.impl;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.data.entity.user.UserCredsEntity;
import sdb.core.data.entity.user.UsersEntity;
import sdb.core.data.repository.UserCredsRepository;
import sdb.core.ex.UserNotFoundException;
import sdb.core.model.auth.RegisterJson;
import sdb.core.model.user.UserDTO;
import sdb.core.service.AuthService;
import utils.logging.Logger;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static sdb.core.data.Databases.transaction;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final Logger logger;
  private final UserCredsRepository userCredsRepository;
  private final SecretKey secretKey;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UserDTO register(@Nonnull RegisterJson json) {
    logger.info("Starting register process for user " + json);
    UserCredsEntity creds = new UserCredsEntity();
    creds.setUsername(json.username());
    creds.setPassword(passwordEncoder.encode(json.password()));

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

    if (!passwordEncoder.matches(json.password(), creds.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    return generateToken(creds.getUsername());
  }

  private String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plusSeconds(86400)))
        .signWith(secretKey)
        .compact();
  }
}
