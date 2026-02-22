package command;

import exception.AccountNotFoundException;
import repository.AccountsRepository;
import model.Account;

public class BlockAccountCommand extends AbstractCommand {
    private int accountId;
    private boolean wasBlocked;

    public BlockAccountCommand(int userId, int accountId) {
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
        account.setIsBlocked(true);
        return "Выполнено: Счет " + getAccountId() + " заблокирован.";
    }
    @Override
    public String undo() throws AccountNotFoundException {
        AccountsRepository.getInstance().findById(getAccountId()).ifPresentOrElse(account -> account.setIsBlocked(getWasBlocked()), () -> new AccountNotFoundException());
        return "Отмена: Статус блокировки счета " + getAccountId() + " возвращен к " + getWasBlocked();
    }
    @Override
    public String toString() {
        return "Command: Блокировка счета. AccountID: " + getAccountId();
    }
}
