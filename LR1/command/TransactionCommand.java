package command;

import java.math.BigDecimal;

import exception.AccountNotFoundException;
import repository.AccountsRepository;
import repository.TransactionsRepository;
import model.Account;
import model.Transaction;
import model.TransactionType;

public class TransactionCommand extends AbstractCommand {
    private int fromAccountId;
    private int toAccountId;
    private BigDecimal amount;
    private int createdTransactionId;

    public TransactionCommand(int userId, int fromAccountId, int toAccountId, BigDecimal amount) {
        super(userId);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public int getFromAccountId() {
        return this.fromAccountId;
    }
    public int getToAccountId() {
        return this.toAccountId;
    }
    public BigDecimal getAmount() {
        return this.amount;
    }
    public int getCreatedTransactionId() {
        return this.createdTransactionId;
    }
    public void setCreatedTransactionId(int id) {
        this.createdTransactionId = id;
    }

    @Override
    public String execute() throws AccountNotFoundException, Exception {
        Account source = AccountsRepository.getInstance().findById(getFromAccountId()).orElseThrow(() -> new AccountNotFoundException());
        Account target = AccountsRepository.getInstance().findById(getToAccountId()).orElseThrow(() -> new AccountNotFoundException());
        source.withdrawal(getAmount());
        target.deposit(getAmount());
        Transaction transaction = new Transaction(getFromAccountId(), getToAccountId(), getAmount(), TransactionType.TRANSFER);
        setCreatedTransactionId(transaction.getId());
        TransactionsRepository.getInstance().save(transaction);
        return "Выполнено: Перевод " + getAmount() + " | " + getFromAccountId() + " -> " + getToAccountId();
    }
    @Override
    public String undo() throws AccountNotFoundException, Exception {
        Account source = AccountsRepository.getInstance().findById(getFromAccountId()).orElseThrow(() -> new AccountNotFoundException());
        Account target = AccountsRepository.getInstance().findById(getToAccountId()).orElseThrow(() -> new AccountNotFoundException());
        target.withdrawal(getAmount());
        source.deposit(getAmount());
        TransactionsRepository.getInstance().deleteById(getFromAccountId(), getCreatedTransactionId());
        TransactionsRepository.getInstance().deleteById(getToAccountId(), getCreatedTransactionId());
        return "Отмена: Перевод " + getCreatedTransactionId() + " аннулирован, средства возвращены.";
    }
    @Override
    public String toString() {
        return "Command: Перевод. " + getFromAccountId() + " -> " + getToAccountId() + " (" + getAmount() + ")";
    }
}
