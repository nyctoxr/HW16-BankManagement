package entity;

import base.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class User extends BaseEntity {
    String firstname;
    String lastname;
    String password;
    String nationalID;
    String phone;
    LocalDate birthdate;
}
