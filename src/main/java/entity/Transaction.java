package entity;

import base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction extends BaseEntity {
    @Column(nullable = false)
    double amount;

    @Column(nullable = false)
    double fee = 600;

    @Column(nullable = false)
    LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "source_card_id", nullable = false)
    Card sourceCard;

    @ManyToOne
    @JoinColumn(name = "destination_card_id", nullable = false)
    Card destinationCard;
}