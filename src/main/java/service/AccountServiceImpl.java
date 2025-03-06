package service;

import base.baseImplements.BaseServiceImpl;
import config.SessionFactoryInstance;
import entity.Account;
import entity.BankBranch;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.repoImplements.AccountRepoImpl;
import repository.repoInterface.AccountRepository;

import java.util.List;

public class AccountServiceImpl extends BaseServiceImpl<Account> {
    private final AccountRepository accountRepository;

    public AccountServiceImpl() {
        super(Account.class);
        this.accountRepository = new AccountRepoImpl();
    }


    public List<Account> getAccountsByCustomerId(Long customerId) {
        try (Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
            return accountRepository.getAllAccountsByCustomerId(session, customerId);
        }
    }
    public void updateInSession(Session session, Account account) {
        accountRepository.update(session, account);
    }

    public Account createAccountInSession(Session session, Customer customer, BankBranch branch, double initialBalance) {
        Account account = new Account();
        account.setBalance(initialBalance);
        account.setCustomer(customer);
        account.setBranch(branch);
        accountRepository.persist(session, account);
        return account;
    }

    public Account createAccount(Customer customer, BankBranch branch, double initialBalance) {
        Transaction tx = null;
        try (Session session = SessionFactoryInstance.getSessionFactory().openSession()) {
            try {
                tx = session.beginTransaction();
                Account account= createAccountInSession(session, customer, branch, initialBalance);
                tx.commit();
                return account;

            }catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new RuntimeException(e);
            }
        }
    }

}
