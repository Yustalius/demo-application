package sdb.core.data.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sdb.core.data.entity.order.OrderEntity;
import sdb.core.model.user.UserDTO;

import java.util.ArrayList;
import java.util.List;

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
  @MapsId
  @JoinColumn(name = "id")
  @JsonManagedReference
  private UserCredsEntity userCreds;

  @OneToMany(mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @JsonBackReference
  private List<OrderEntity> purchases = new ArrayList<>();

  public static UsersEntity fromDto(UserDTO userDTO) {
    UsersEntity entity = new UsersEntity();
    entity.setId(userDTO.id());
    entity.setFirstName(userDTO.firstName());
    entity.setLastName(userDTO.lastName());
    entity.setAge(userDTO.age());

    return entity;
  }
}
