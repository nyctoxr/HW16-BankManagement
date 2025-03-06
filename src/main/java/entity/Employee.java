package entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee extends User {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    BankBranch branch;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Employee manager;
}

