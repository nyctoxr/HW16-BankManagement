package entity;

import base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
    @Column(nullable = false, unique = true, length = 16)
    @ Pattern(regexp = "^\\d{12}|\\d{16}$", message = "Card number must be 12 or 16 digits")
    String cardNumber;

    @Column(nullable = false, length = 4)
    @Pattern(regexp = "^\\d{3,4}$", message = "CVV2 must be 3 or 4 digits")
    String cvv2;

    @Column(nullable = false, length = 100)
    @Pattern(regexp = "^\\d{4}$", message = "PIN must be exactly 4 digits")
    String pin;

    @Column(nullable = false)
    LocalDate expiryDate;

    @Column(nullable = false)
    boolean isBlocked = false;

    @Column(nullable = false)
    int failedAttempts = 0;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account;

    @OneToMany(mappedBy = "sourceCard")
    List<Transaction> sourceTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "destinationCard")
    List<Transaction> destinationTransactions = new ArrayList<>();
}