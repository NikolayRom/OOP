package model;
import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Account implements Identifiable {
    protected int id;
    protected int userId;
    protected int bankId;
    protected LocalDate dateCreated;
    protected BigDecimal balance;
    protected boolean isBlocked;

    Account(int userId, int bankId) {
        this.id = IdGen.getInstance().newId();
        this.userId = userId;
        this.bankId = bankId;
        this.dateCreated = LocalDate.now();
        this.balance = BigDecimal.ZERO;
        this.isBlocked = false;
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
    public boolean isBlocked() {
        return this.isBlocked;
    }
    public abstract void deposit(BigDecimal amount);
}
