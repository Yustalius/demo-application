package sdb.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.app.data.entity.product.ProductEntity;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

}
