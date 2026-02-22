package repository;

import model.Bank;

public class BanksRepository extends MemoryManager<Bank> {
    private static BanksRepository instance;
    private BanksRepository() {}
    public static BanksRepository getInstance() {
        if(instance == null) {
            instance = new BanksRepository();
        }
        return instance;
    }
}
