package sdb.core.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.core.data.entity.user.UserCredsEntity;

import java.util.Optional;

@Repository
public interface UserCredsRepository extends JpaRepository<UserCredsEntity, Integer> {
  Optional<UserCredsEntity> findByUsername(String username);

  UserCredsEntity findByUsernameAndPassword(String username, String password);
}
