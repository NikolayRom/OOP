package service;

public class AdminService {
    private static AdminService instance;
    private AdminService() {}
    public static AdminService getInstance() {
        if(instance == null) {
            instance = new AdminService();
        }
        return instance;
    }
}
