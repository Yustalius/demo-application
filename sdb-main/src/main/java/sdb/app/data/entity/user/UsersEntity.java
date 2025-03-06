package sdb.app.data.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UsersEntity {
  @Id
  private Integer id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(nullable = false)
  private Integer age;

  @OneToOne
  @MapsId // id в UsersEntity совпадает с id в UserCredsEntity
  @JoinColumn(name = "id") // Внешний ключ
  private UserCredsEntity userCreds;
}
