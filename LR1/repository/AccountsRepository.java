package repository;
import model.Account;

public class AccountsRepository extends MemoryManager<Account> {
    private static AccountsRepository instance;
    private AccountsRepository() {};
    public static AccountsRepository getInstance() {
        if(instance == null) instance = new AccountsRepository();
        return instance;
    }
}
