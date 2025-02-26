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
    double amount;
    double fee = 600;
    LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "source_card_id")
    Card sourceCard;

    @ManyToOne
    @JoinColumn(name = "destination_card_id")
    Card destinationCard;
}