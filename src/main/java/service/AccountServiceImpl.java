package service;

import base.BaseServiceImpl;
import config.SessionFactoryInstance;
import entity.Account;
import org.hibernate.Session;
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
}
