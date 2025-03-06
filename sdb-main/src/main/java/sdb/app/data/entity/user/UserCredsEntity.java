package sdb.app.data.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_creds")
@Getter
@Setter
public class UserCredsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String username;

  @Column(name = "pass", nullable = false)
  private String password;

  @OneToOne(mappedBy = "userCreds", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private UsersEntity user;
}
