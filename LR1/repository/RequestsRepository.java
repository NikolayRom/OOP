package repository;
import model.Request;

public class RequestsRepository extends MemoryManager<Request> {
    private static RequestsRepository instance;
    private RequestsRepository() {}
    public static RequestsRepository getInstance() {
        if(instance == null) instance = new RequestsRepository();
        return instance;
    }    
}
