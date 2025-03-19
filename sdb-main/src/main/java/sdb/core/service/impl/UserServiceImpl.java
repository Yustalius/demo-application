package sdb.core.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.data.entity.user.UsersEntity;
import sdb.core.data.repository.UserCredsRepository;
import sdb.core.data.repository.UsersRepository;
import sdb.core.ex.UserNotFoundException;
import sdb.core.model.user.UserDTO;
import org.springframework.stereotype.Service;
import sdb.core.service.UserService;
import utils.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final Logger logger;
  private final UsersRepository usersRepository;
  private final UserCredsRepository userCredsRepository;

  @Override
  public void create(UserDTO json) {
    usersRepository.save(UsersEntity.fromDto(json));
  }

  @Override
  @Transactional(readOnly = true)
  public UserDTO get(int userId) {
    return usersRepository.findById(userId)
        .map(UserDTO::fromEntity)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }

  @Override
  @Transactional
  public void delete(int userId) {
    userCredsRepository.findById(userId)
        .map(user -> {
          userCredsRepository.deleteById(user.getId());
          return user;
        })
        .orElseThrow(() -> new UserNotFoundException(userId));

    logger.info("User id = %s deleted".formatted(userId));
  }

  @Override
  public UserDTO update(int userId, @Nonnull UserDTO update) {
    logger.info("Update user id = %s, %s".formatted(userId, update));
    return usersRepository.findById(userId).map(user -> {
          if (update.firstName() != null) {
            user.setFirstName(update.firstName());
          }
          if (update.lastName() != null) {
            user.setLastName(update.lastName());
          }
          if (update.age() != null) {
            user.setAge(update.age());
          }

          return UserDTO.fromEntity(usersRepository.save(user));
        }
    ).orElseThrow(() -> new UserNotFoundException(userId));
  }
}
