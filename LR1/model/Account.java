package model;
import java.math.BigDecimal;
import java.time.LocalDate;

import exception.InsufficientFundsException;

public abstract class Account implements Identifiable {
    protected int id;
    protected int userId;
    protected int bankId;
    protected LocalDate dateCreated;
    protected BigDecimal balance;
    protected boolean isBlocked;
    protected AccountType type;

    public Account(int userId, int bankId, AccountType type) {
        this.id = IdGen.getInstance().newId();
        this.userId = userId;
        this.bankId = bankId;
        this.dateCreated = LocalDate.now();
        this.balance = BigDecimal.ZERO;
        this.isBlocked = false;
        this.type = type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return this.userId;
    }
    public int getBankId() {
        return this.bankId;
    }
    public LocalDate getDateCreated() {
        return this.dateCreated;
    }
    public BigDecimal getBalance() {
        return this.balance;
    }
    public boolean getIsBlocked() {
        return this.isBlocked;
    }

    public void setBalance(BigDecimal amount) throws InsufficientFundsException {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        this.balance = amount;
    }

    public abstract void deposit(BigDecimal amount) throws Exception;
    public abstract void withdrawal(BigDecimal amount) throws Exception;
}
