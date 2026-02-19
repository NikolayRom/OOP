package repository;
import model.Transaction;

public class TransactionsRepository extends MemoryManager<Transaction> {
    private static TransactionsRepository instance;
    private TransactionsRepository() {}
    public static TransactionsRepository getInstance() {
        if(instance == null) instance = new TransactionsRepository();
        return instance;
    }
}
