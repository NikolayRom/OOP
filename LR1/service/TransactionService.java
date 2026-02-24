package service;

import repository.TransactionsRepository;
import model.Transaction;
import java.util.List;

public class TransactionService {
    private static TransactionService instance;
    private TransactionService() {}
    public static TransactionService getInstance() {
        if(instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public List<Transaction> getHistoryForAccount(int accountId) {
        return TransactionsRepository.getInstance().getByAccountId(accountId);
    }
}
