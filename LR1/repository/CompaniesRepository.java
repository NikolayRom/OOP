package repository;
import model.Company;

public class CompaniesRepository extends MemoryManager<Company> {
    private static CompaniesRepository instance;
    private CompaniesRepository() {}
    public static CompaniesRepository getInstance() {
        if(instance == null) instance = new CompaniesRepository();
        return instance;
    }
}
