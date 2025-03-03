package repository.repoInterface;

import base.BaseRepository;
import entity.Account;
import org.hibernate.Session;

import java.util.List;

public interface AccountRepository extends BaseRepository<Account,Long> {
    List<Account> getAllAccountsByCustomerId(Session session, Long customerId);
}
