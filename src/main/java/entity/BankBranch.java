package entity;

import base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankBranch extends BaseEntity {
    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String address;

    @OneToOne
    @JoinColumn(name = "manager_id", nullable = false)
    Employee manager;

    @OneToMany(mappedBy = "branch")
    List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "branch")
    List<Account> accounts = new ArrayList<>();
}