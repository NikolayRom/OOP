package model;

import java.math.BigDecimal;
import exception.*;

public class DepositAccount extends Account {
    private BigDecimal interestRate;
    private int durationInMonth;
    private boolean isClosed;
    private boolean isEndDepositDuration;

    public DepositAccount(int userId, int bankId, BigDecimal interestRate, int durationInMonth) {
        super(userId, bankId, AccountType.DEPOSIT);
        this.interestRate = interestRate;
        this.durationInMonth = durationInMonth;
        this.isClosed = false;
        this.isEndDepositDuration = false;
    }

    @Override
    public void deposit(BigDecimal amount) throws ClosedDepositAccountException, BlockedAccountException, InsufficientFundsException, EndDepositDurationException {
        if(getIsEndDepositDuration()) {
            throw new EndDepositDurationException();
        }
        if(getIsClosed()) {
            throw new ClosedDepositAccountException();
        }
        if(getIsBlocked()) {
            throw new BlockedAccountException();
        }
        setBalance(getBalance().add(amount));
        setIsClosed(true);
    }

    @Override
    public void withdrawal(BigDecimal amount) throws BlockedAccountException, InsufficientFundsException, ClosedDepositAccountException {
        if(getIsBlocked()) {
            throw new BlockedAccountException();
        }
        if(getIsBlocked()) {
            throw new ClosedDepositAccountException();
        }
        
        setBalance(getBalance().subtract(amount));
        
    }

    public void accumulation() throws EndDepositDurationException, InsufficientFundsException {
        if(getIsEndDepositDuration()) {
            throw new EndDepositDurationException();
        }
        setBalance(getBalance().add(getBalance().multiply(getInterestRate())));
        setDurationInMonth(getDurationInMonth() - 1);
        if(getDurationInMonth() == 0) {
            setIsEndDepositDuration(true);
            setIsClosed(false);
        }
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }
    public int getDurationInMonth() {
        return this.durationInMonth;
    }
    public void setDurationInMonth(int num) {
        if(num < 0) {
            this.durationInMonth = 0;
        } else {
            this.durationInMonth = num;
        }
    }
    public boolean getIsClosed() {
        return this.isClosed;
    }
    public void setIsClosed(boolean stat) {
        this.isClosed = stat;
    }
    public boolean getIsEndDepositDuration() {
        return this.isEndDepositDuration;
    }
    public void setIsEndDepositDuration(boolean stat) {
        this.isEndDepositDuration = stat;
    }

    // public void transfer(BigDecimal amount, int accountId) throws NotFoundAccountException, ClosedDepositAccountException, BlockedAccountException, InsufficientFundsException, Exception {
    //     if(AccountsRepository.getInstance().findById(accountId).equals(Optional.empty())) {
    //         throw new NotFoundAccountException();
    //     }
    //     try {
    //         withdrawal(amount);
    //     } catch(Exception ex) {
    //         throw ex;
    //     }
    //     try {
    //         AccountsRepository.getInstance().findById(accountId).get().accrual(amount);
    //     } catch(Exception ex) {
    //         accrual(amount);
    //         throw ex;
    //     }
        
    // }

}
