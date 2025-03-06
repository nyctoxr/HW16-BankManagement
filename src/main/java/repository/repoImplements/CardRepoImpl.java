package repository.repoImplements;

import base.baseImplements.BaseRepositoryImpl;
import entity.Card;
import org.hibernate.Session;
import org.hibernate.query.Query;
import repository.repoInterface.CardRepository;

public class CardRepoImpl extends BaseRepositoryImpl<Card> implements CardRepository {

    @Override
    public Card findByCardNumber(Session session, String cardNumber) {
        Query<Card> query = session.createQuery("from Card where cardNumber = :cardNumber" , Card.class);
        query.setParameter("cardNumber", cardNumber);
        return query.uniqueResult();
    }
}
