package sdb.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.app.data.entity.user.UsersEntity;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {

}
