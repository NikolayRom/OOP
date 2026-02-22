package command;

import java.math.BigDecimal;
import model.AccountType;
import model.Account;
import model.DebitAccount;
import model.DepositAccount;
import repository.AccountsRepository;

public class OpenAccountCommand extends AbstractCommand {
    private int bankId;
    private AccountType type;
    private BigDecimal interestRate;
    private int durationInMonth;
    private int createdAccountId;

    public OpenAccountCommand(int userId, int bankId, AccountType type, BigDecimal interestRate, int durationInMonth) {
        super(userId);
        this.bankId = bankId;
        this.type = type;
        this.interestRate = interestRate;
        this.durationInMonth = durationInMonth;
    }
    public OpenAccountCommand(int userId, int bankId, AccountType type) {
        super(userId);
        this.bankId = bankId;
        this.type = type;
        this.interestRate = BigDecimal.ZERO;
        this.durationInMonth = 0;
    }

    public int getBankId() {
        return this.bankId;
    }
    public AccountType getType() {
        return this.type;
    }
    public BigDecimal getInterestRate() {
        return this.interestRate;
    }
    public int getDurationInMonth() {
        return this.durationInMonth;
    }
    public int getCreatedAccountId() {
        return this.createdAccountId;
    }
    public void setCreatedAccountId(int id) {
        this.createdAccountId = id;
    }

    @Override
    public String execute() {
        Account newAccount = switch(getType()) {
            case AccountType.DEPOSIT:
                yield new DepositAccount(userId, bankId, interestRate, durationInMonth);
            case AccountType.DEBIT:
                yield new DebitAccount(userId, bankId);
        };
        AccountsRepository.getInstance().save(newAccount);
        setCreatedAccountId(newAccount.getId());
        return "Выполнено: Открыт счет " + getCreatedAccountId() + " типа " + getType();
    }
    @Override
    public String undo() {
        AccountsRepository.getInstance().deleteById(getCreatedAccountId());
        return "Отмена: Счет " + getCreatedAccountId() + " удален.";
    }
    @Override
    public String toString() {
        return "Command: Открытие счета. User: " + getUserId() + ", Type: " + getType();
    }
}
