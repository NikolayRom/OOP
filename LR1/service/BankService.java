package service;

import repository.BanksRepository;
import model.Bank;
import java.util.List;
import exception.*;

public class BankService {
    private static BankService instance;
    private BankService() {}
    public static BankService getInstance() {
        if(instance == null) {
            instance = new BankService();
        }
        return instance;
    }
    
    public List<Bank> getAllBanks() {
        return BanksRepository.getInstance().getAll();
    }
    public Bank getBankById(int id) throws BankNotFoundException {
        return BanksRepository.getInstance().findById(id).orElseThrow(() -> new BankNotFoundException());
    }
    public void createBank(String name) {
        BanksRepository.getInstance().save(new Bank(name));
    }
}
