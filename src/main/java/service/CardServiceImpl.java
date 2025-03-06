package service;

import base.baseImplements.BaseServiceImpl;
import config.SessionFactoryInstance;
import entity.Account;
import entity.Card;
import entity.Transaction;
import exceptions.CardNotFoundException;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidCardDetailsException;
import org.hibernate.Session;
import repository.repoImplements.CardRepoImpl;
import repository.repoInterface.CardRepository;
import util.ValidatorUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class CardServiceImpl extends BaseServiceImpl<Card>{
    private final CardRepository cardRepository = new CardRepoImpl();
    private final AccountServiceImpl accountService = new AccountServiceImpl();
    private final TransactionServiceImpl transactionService = new TransactionServiceImpl();

    public CardServiceImpl() {
        super(Card.class);
    }


    public Card createCardInSession(Session session, Account account, String pin) throws IllegalArgumentException {
        Random random = new Random();

        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }

        StringBuilder cvv2 = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            cvv2.append(random.nextInt(10));
        }

        LocalDate expiryDate = LocalDate.now().plusYears(5);

        Card card = new Card();
        card.setCardNumber(cardNumber.toString());
        card.setCvv2(cvv2.toString());
        card.setPin(pin);
        card.setExpiryDate(expiryDate);
        card.setAccount(account);

        if (!ValidatorUtil.validate(card)) {
            throw new IllegalArgumentException("Card validation failed");
        }

        cardRepository.persist(session, card);
        return card;
    }

    public Card createCard(Account account, String pin) throws IllegalArgumentException {
        org.hibernate.Transaction tx =null;
        try(Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
            try {
                tx = session.beginTransaction();
                Card card = createCardInSession(session, account, pin);
                tx.commit();
                return card;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new RuntimeException("unexpected exception", e);
            }
        }
    }


    public Transaction transferCardToCard(String sourceCardNumber,
                                          String destinationCardNumber, Double amount,
                                          String pin, String cvv2, LocalDate expiryDate)
            throws InvalidCardDetailsException, CardNotFoundException, InsufficientBalanceException {
        org.hibernate.Transaction tx = null;

        try(Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Card sourceCard = cardRepository.findByCardNumber(session, sourceCardNumber);
            if (!(sourceCardNumber.length() ==12||sourceCardNumber.length()==16)) {
                throw new InvalidCardDetailsException("Source card number must be 12 or 16 digits");
            }
            if (sourceCard == null) {
                throw new CardNotFoundException("the sourceCard Not Found");
            }


            Card destinationCard = cardRepository.findByCardNumber(session, destinationCardNumber);
            if(!(destinationCardNumber.length() ==12||destinationCardNumber.length()==16)) {
                throw new CardNotFoundException("the destionation Not Found");
            }

            if (destinationCard == null) {
                throw new CardNotFoundException("the destinationCard Not Found");
            }


            if(sourceCard.isBlocked()){
                throw new InvalidCardDetailsException("your card is blocked");
            }

            if(destinationCard.isBlocked()){
                throw new InvalidCardDetailsException("the destination card is blocked");
            }

            if (!sourceCard.getCvv2().equals(cvv2)) {
                throw new InvalidCardDetailsException("Incorrect CVV2");
            }
            if (!sourceCard.getExpiryDate().equals(expiryDate)) {
                throw new InvalidCardDetailsException("Incorrect expiry date");
            }
            if(!checkAndBlockCard(sourceCard,pin)){
                throw new InvalidCardDetailsException("Card is blocked");
            }

            double fee=600.0;
            double totalAmount=amount+fee;
            if(sourceCard.getAccount().getBalance()<totalAmount){
                throw new InsufficientBalanceException("Insufficient balance in source account");
            }

            sourceCard.getAccount().setBalance(sourceCard.getAccount().getBalance()-totalAmount);
            accountService.updateInSession(session,sourceCard.getAccount());

            destinationCard.getAccount().setBalance(destinationCard.getAccount().getBalance()+amount);
            accountService.updateInSession(session,destinationCard.getAccount());

            Transaction cartTransaction = new Transaction();
            cartTransaction.setSourceCard(sourceCard);
            cartTransaction.setDestinationCard(destinationCard);
            cartTransaction.setAmount(amount);
            cartTransaction.setFee(fee);
            cartTransaction.setTransactionDate(LocalDateTime.now());

            transactionService.createTransactionInSession(session,cartTransaction);

            tx.commit();
            return cartTransaction;

        }catch (CardNotFoundException | InsufficientBalanceException | InvalidCardDetailsException e){
            if(tx!=null){
                tx.rollback();
            }
            throw e;
        }catch (Exception e){
            if(tx!=null){
                tx.rollback();
            }
            throw new RuntimeException("Unexpected error during transfer: " + e.getMessage(), e);
        }

    }

    public void updatePin(String cardNumber, String newpin)
            throws InvalidCardDetailsException, CardNotFoundException {
        org.hibernate.Transaction tx = null;
        try(Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            if(!(cardNumber.length() ==12||cardNumber.length()==16)) {
                throw new CardNotFoundException("the destionation Not Found");
            }

            Card card = cardRepository.findByCardNumber(session, cardNumber);
            if(card==null){
                throw new CardNotFoundException("card not found");
            }
            if (newpin==null || newpin.length()!=4) {
                throw new InvalidCardDetailsException("pin must be 4 digit ");
            }
            if (card.getPin().equals(newpin)) {
                throw new InvalidCardDetailsException("your entered pin must be different ");
            }
            card.setPin(newpin);
            cardRepository.update(session, card);
            tx.commit();

        } catch (InvalidCardDetailsException|CardNotFoundException e) {
            if(tx!=null){
                tx.rollback();
            }
            throw e;
        } catch (Exception e) {
            if(tx!=null){
                tx.rollback();
            }
            System.out.println("Unexpected error during updatePin: " + e.getMessage());
        }
    }

    public Boolean checkAndBlockCard(Card card,String pin)
            throws CardNotFoundException, InvalidCardDetailsException {

        org.hibernate.Transaction tx = null;
        try(Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            if (!card.getPin().equals(pin)) {
                int failedAttempts = card.getFailedAttempts()+1;
                card.setFailedAttempts(failedAttempts);
                if(card.getFailedAttempts()==3){
                    card.setBlocked(true);
                    System.out.println("Card is blocked");
                } else {
                    System.out.println("Incorrect PIN. Failed attempts: " + failedAttempts);
                }
                cardRepository.update(session, card);
                tx.commit();
                return false;
            }else{
                card.setFailedAttempts(0);
                cardRepository.update(session, card);
                tx.commit();
                return true;
            }

        }catch (Exception e){
            if(tx!=null){
                tx.rollback();
            }
            throw new RuntimeException("Unexpected error during checkAndBlockCard: " + e.getMessage(), e);
        }
    }

}
