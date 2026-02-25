package model;

import java.math.BigDecimal;
import exception.*;

public class DebitAccount extends Account{

    public DebitAccount(int userId, int bankId) {
        super(userId, bankId, AccountType.DEBIT);
    }

    @Override
    public void deposit(BigDecimal amount) throws BlockedAccountException, InsufficientFundsException {
        if(getIsBlocked()) {
            throw new BlockedAccountException();
        }
        setBalance(getBalance().add(amount));
    }

    @Override
    public void withdrawal(BigDecimal amount) throws BlockedAccountException, InsufficientFundsException {
        if(getIsBlocked()) {
            throw new BlockedAccountException();
        }
        setBalance(getBalance().subtract(amount));
       
    }
}
