package repository.repoInterface;

import base.BaseRepository;
import entity.Card;
import org.hibernate.Session;

public interface CardRepository extends BaseRepository<Card,Long> {
    Card findByCardNumber(Session session, String cardNumber);
}
