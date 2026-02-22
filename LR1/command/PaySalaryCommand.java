package command;

import java.math.BigDecimal;

import repository.AccountsRepository;
import repository.TransactionsRepository;
import model.Account;
import model.Transaction;
import model.TransactionType;
import exception.*;

public class PaySalaryCommand extends AbstractCommand {
    private int targetAccountId;
    private BigDecimal amount;
    private int createdTransactionId;

    public PaySalaryCommand(int userId, int targetAccountId, BigDecimal amount) {
        super(userId);
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public int getTargetAccountId() {
        return this.targetAccountId;
    }
    public BigDecimal getAmount() {
        return this.amount;
    }
    public int getCreatedTransactionId() {
        return createdTransactionId;
    }
    public void setCreatedTransactionId(int id) {
        this.createdTransactionId = id;
    }

    @Override
    public String execute() throws AccountNotFoundException, Exception {
        Account account = AccountsRepository.getInstance().findById(getTargetAccountId()).orElseThrow(() -> new AccountNotFoundException());
        account.deposit(getAmount());
        Transaction tx = new Transaction(-1, getTargetAccountId(), getAmount(), TransactionType.SALARY_PAYMENT);
        setCreatedTransactionId(tx.getId());
        TransactionsRepository.getInstance().save(tx);
        return "Выполнено: Выплачена зарплата " + getAmount() + " на счет " + getTargetAccountId();

    }
    @Override
    public String undo() throws AccountNotFoundException, Exception {
        Account account = AccountsRepository.getInstance().findById(getTargetAccountId()).orElseThrow(() -> new AccountNotFoundException());
        account.withdrawal(getAmount());
        TransactionsRepository.getInstance().deleteById(getTargetAccountId(), getCreatedTransactionId());
        return "Отмена: Зарплата отозвана, транзакция " + getCreatedTransactionId() + " удалена.";
    }
    @Override
    public String toString() {
        return "Command: Выплата зарплаты. Acc: " + getTargetAccountId() + ", Amount: " + getAmount();
    }
}
