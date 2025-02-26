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
    String cardNumber; // مثلاً 12 یا 16 رقمی
    String cvv2;
    String pin; // رمز دوم
    LocalDate expiryDate;
    boolean isBlocked = false;
    int failedAttempts = 0; // برای مسدود شدن بعد از 3 بار اشتباه

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;

    @OneToMany(mappedBy = "sourceCard")
    List<Transaction> sourceTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "destinationCard")
    List<Transaction> destinationTransactions = new ArrayList<>();
}