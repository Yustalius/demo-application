package sdb.core.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.core.data.entity.user.UsersEntity;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {

}
