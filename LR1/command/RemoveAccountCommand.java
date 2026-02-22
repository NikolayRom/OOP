package command;

import exception.AccountNotFoundException;
import model.Account;
import repository.AccountsRepository;

public class RemoveAccountCommand extends AbstractCommand {
    private int accountId;
    private Account backupAccount;

    public RemoveAccountCommand(int userId, int accountId) {
        super(userId);
        this.accountId = accountId;
    }

    public int getAccountId() {
        return this.accountId;
    }
    public Account getBackupAccount() {
        return this.backupAccount;
    }
    public void setBackupAccount(Account account) {
        this.backupAccount = account;
    }

    @Override
    public String execute() throws AccountNotFoundException {
        Account accountToDelete = AccountsRepository.getInstance().findById(getAccountId()).orElseThrow(() -> new AccountNotFoundException());
        setBackupAccount(accountToDelete);
        AccountsRepository.getInstance().deleteById(getAccountId());
        return "Выполнено: Счет " + getAccountId() + " закрыт (удален).";
    }
    @Override
    public String undo() {
        AccountsRepository.getInstance().save(getBackupAccount());
        return "Отмена: Счет " + getAccountId() + " восстановлен.";
    }
    @Override
    public String toString() {
        return "Command: Закрытие счета. AccountID: " + getAccountId();
    }
}
