package sdb.core.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.config.SecurityProperties;
import sdb.core.data.entity.user.UsersEntity;
import sdb.core.data.repository.UserCredsRepository;
import sdb.core.data.repository.UsersRepository;
import sdb.core.ex.PermissionDeniedException;
import sdb.core.ex.UserNotFoundException;
import sdb.core.model.user.UserDTO;
import org.springframework.stereotype.Service;
import sdb.core.service.UserService;
import utils.logging.Logger;
import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final Logger logger;
  private final UsersRepository usersRepository;
  private final UserCredsRepository userCredsRepository;
  private final SecurityProperties securityProperties;

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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();

    // Проверяем, является ли текущий пользователь администратором
    boolean isAdmin = securityProperties.getAdmin().getUsernames().contains(currentUsername);

    userCredsRepository.findById(userId)
        .map(user -> {
          // Разрешаем удаление, если это администратор или владелец аккаунта
          if (!isAdmin && !currentUsername.equals(user.getUsername())) {
            logger.warn("Unauthorized delete attempt by user: %s".formatted(currentUsername));
            throw new PermissionDeniedException();
          }
          userCredsRepository.deleteById(user.getId());
          return user;
        })
        .orElseThrow(() -> {
          logger.warn("User not found for deletion: id = %s".formatted(userId));
          return new UserNotFoundException(userId);
        });

    logger.info("User id = %s deleted by %s (admin: %s)".formatted(userId, currentUsername, isAdmin));
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
