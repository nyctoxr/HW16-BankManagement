package entity;

import base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends BaseEntity {
    @Column(nullable = false)
    double balance;

    @Column(nullable = false)
    boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    BankBranch branch;

    @OneToOne(mappedBy = "account")
    Card card;
}