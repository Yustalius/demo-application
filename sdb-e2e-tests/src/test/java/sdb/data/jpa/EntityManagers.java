package sdb.data.jpa;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static sdb.data.Databases.dataSource;

public class EntityManagers {
  private EntityManagers() {
  }

  private static final Map<String, EntityManagerFactory> emfs = new ConcurrentHashMap<>();

  @SuppressWarnings("resource")
  @Nonnull
  public static EntityManager em(@Nonnull String jdbcUrl) {
    return new ThreadSafeEntityManager(
        emfs.computeIfAbsent(
            jdbcUrl,
            key -> {
              dataSource(jdbcUrl);
              return Persistence.createEntityManagerFactory(jdbcUrl);
            }
        ).createEntityManager()
    );
  }

  public static void closeAllEmfs() {
    emfs.values().forEach(EntityManagerFactory::close);
  }
}
