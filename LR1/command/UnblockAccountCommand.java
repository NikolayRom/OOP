package command;

import exception.AccountNotFoundException;
import model.Account;
import repository.AccountsRepository;

public class UnblockAccountCommand extends AbstractCommand {
    private int accountId;
    private boolean wasBlocked;

    public UnblockAccountCommand(int userId, int accountId) {
        super(userId);
        this.accountId = accountId;
    }

    public int getAccountId() {
        return this.accountId;
    }  
    public boolean getWasBlocked() {
        return this.wasBlocked;
    }
    public void setWasBlocked(boolean bool) {
        this.wasBlocked = bool;
    }

    @Override
    public String execute() throws AccountNotFoundException {
        Account account = AccountsRepository.getInstance().findById(getAccountId()).orElseThrow(() -> new AccountNotFoundException());
        setWasBlocked(account.getIsBlocked());
        account.setIsBlocked(false);
        return "Выполнено: Счет " + getAccountId() + " разблокирован.";
    }
    @Override
    public String undo() throws AccountNotFoundException {
        AccountsRepository.getInstance().findById(getAccountId()).ifPresentOrElse(account -> account.setIsBlocked(getWasBlocked()), () -> new AccountNotFoundException());
        return "Отмена: Статус блокировки счета " + getAccountId() + " возвращен к " + getWasBlocked();
    }
    @Override
    public String toString() {
        return "Command: Разблокировка счета. AccountID: " + getAccountId();
    }
}
