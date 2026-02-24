package repository;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import model.Transaction;

public class TransactionsRepository {
    private static TransactionsRepository instance;
    private HashMap<Integer, List<Transaction>> storage = new HashMap<>();
    private TransactionsRepository() {
        this.storage = new HashMap<>();
    }
    public static TransactionsRepository getInstance() {
        if(instance == null) instance = new TransactionsRepository();
        return instance;
    }

    public void save(Transaction transaction) {
        if (transaction.getFromAccountId() >= 0) {
            storage.computeIfAbsent(transaction.getFromAccountId(), k -> new ArrayList<>()).add(transaction);
        }
        if (transaction.getToAccountId() >= 0 && transaction.getFromAccountId() != transaction.getToAccountId()) {
            storage.computeIfAbsent(transaction.getToAccountId(), k -> new ArrayList<>()).add(transaction);
        }
    }

    public Optional<Transaction> deleteById(int accountId, int transactionId) {
        if(!storage.containsKey(accountId) || storage.get(accountId) == null || storage.get(accountId).isEmpty()) {
            return Optional.empty();
        }
        Optional<Transaction> transaction = storage.get(accountId).stream().filter(ent -> ent.getId() == transactionId).findFirst();
        if(transaction.isPresent()) {
            storage.get(accountId).remove(transaction.get());
            return transaction;
        }
        return Optional.empty();
    }

    public Optional<Transaction> findById(int accountId, int transactionId) {
        if(!storage.containsKey(accountId) || storage.get(accountId) == null || storage.get(accountId).isEmpty()) {
            return Optional.empty();
        }
        return storage.get(accountId).stream().filter(ent -> ent.getId() == transactionId).findFirst();
    }
    public List<Transaction> getByAccountId(int accountId) {
        return storage.getOrDefault(accountId, Collections.emptyList());
    }
}
