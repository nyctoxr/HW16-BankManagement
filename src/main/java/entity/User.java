package entity;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Pattern;
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
    @Column(nullable = false, length = 50)
    String firstname;

    @Column(nullable = false, length = 50)
    String lastname;

    @Column(nullable = false, length = 100)
    String password;

    @Column(nullable = false, unique = true, length = 10)
    @Pattern(regexp = "^\\d{10}$", message = "National ID must be exactly 10 digits")
    String nationalID;

    @Column(nullable = false, unique = true, length = 11)
    @Pattern(regexp = "^09\\d{9}$", message = "Phone number must start with 09 and be 11 digits")
    String phone;

    @Column(nullable = false)
    LocalDate birthdate;
}
