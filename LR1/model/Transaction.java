package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction implements Identifiable {
    private int id;
    private int fromAccountId;
    private int toAccountId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate dateCreated;

    public Transaction(int fromAccountId, int toAccountId, BigDecimal amount, TransactionType type) {
        this.id = IdGen.getInstance().newId();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = type;
        this.dateCreated = LocalDate.now();
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
    public TransactionType getType() {
        return this.type;
    }
    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public String toString() {
        return String.format("[%s] %s: %s -> %s | Сумма: %s", getDateCreated(), getType(), getFromAccountId(), getToAccountId(), getAmount());
    }
}
