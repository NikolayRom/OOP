package repository;
import model.User;

public class UsersRepository extends MemoryManager<User> {
    private static UsersRepository instance;
    private UsersRepository() {}
    public static UsersRepository getInstance() {
        if(instance == null) instance = new UsersRepository();
        return instance;
    }
}
