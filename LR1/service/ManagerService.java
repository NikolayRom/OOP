package service;

public class ManagerService {
    private static ManagerService instance;
    private ManagerService() {}
    public static ManagerService getInstance() {
        if(instance == null) {
            instance = new ManagerService();
        }
        return instance;
    }

    
}
