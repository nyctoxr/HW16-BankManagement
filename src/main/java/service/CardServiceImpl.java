package service;

import base.BaseServiceImpl;
import config.SessionFactoryInstance;
import entity.Card;
import entity.Transaction;
import exceptions.CardNotFoundException;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidCardDetailsException;
import org.hibernate.Session;
import repository.repoImplements.CardRepoImpl;
import repository.repoInterface.CardRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardServiceImpl extends BaseServiceImpl<Card>{
    private final CardRepository cardRepository = new CardRepoImpl();
    private final AccountServiceImpl accountService = new AccountServiceImpl();
    private final TransactionServiceImpl transactionService = new TransactionServiceImpl();

    public CardServiceImpl() {
        super(Card.class);
    }

    public Transaction transferCardToCard(String sourceCardNumber,
                                          String destinationCardNumber, Double amount,
                                          String pin, String cvv2, LocalDate expiryDate) throws InvalidCardDetailsException, CardNotFoundException, InsufficientBalanceException {
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
            if (!sourceCard.getPin().equals(pin)) {
                throw new InvalidCardDetailsException("Incorrect PIN");
            }
            if (!sourceCard.getCvv2().equals(cvv2)) {
                throw new InvalidCardDetailsException("Incorrect CVV2");
            }
            if (!sourceCard.getExpiryDate().equals(expiryDate)) {
                throw new InvalidCardDetailsException("Incorrect expiry date");
            }

            double fee=600.0;
            double totalAmount=amount+fee;
            if(sourceCard.getAccount().getBalance()>totalAmount){
                throw new InsufficientBalanceException("Insufficient balance in source account");
            }

            sourceCard.getAccount().setBalance(sourceCard.getAccount().getBalance()-amount);
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


}
