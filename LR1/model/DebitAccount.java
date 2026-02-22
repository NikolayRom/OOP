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
