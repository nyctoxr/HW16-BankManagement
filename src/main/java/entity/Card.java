package entity;

import base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card extends BaseEntity {
    String cardNumber;
    String cvv2;
    String pin;
    LocalDate expiryDate;
    boolean isBlocked = false;
    int failedAttempts = 0;

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;

    @OneToMany(mappedBy = "sourceCard")
    List<Transaction> sourceTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "destinationCard")
    List<Transaction> destinationTransactions = new ArrayList<>();
}