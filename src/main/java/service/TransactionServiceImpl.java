package service;

import base.baseImplements.BaseServiceImpl;
import entity.Transaction;
import org.hibernate.Session;
import repository.repoImplements.TransactionRepoImpl;
import repository.repoInterface.TransactionRepository;

public class TransactionServiceImpl extends BaseServiceImpl<Transaction> {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl() {
        super(Transaction.class);
        this.transactionRepository = new TransactionRepoImpl();
    }

    public void createTransactionInSession(Session session,Transaction transaction) {
        transactionRepository.persist(session,transaction);
    }
}
