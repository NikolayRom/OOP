package service;

import model.CompanyType;
import repository.CompaniesRepository;
import repository.UsersRepository;
import model.Company;
import model.User;
import java.util.List;
import java.util.ArrayList;

import exception.CompanyNotFoundException;

public class CompanyService {
    private static CompanyService instance;
    private CompanyService() {}
    public static CompanyService getInstance() {
        if(instance == null) {
            instance = new CompanyService();
        }
        return instance;
    }

    public void createCompany(String name, CompanyType type) {
        CompaniesRepository.getInstance().save(new Company(name, type));
    }
    public List<Company> getAllCompanies() {
        return CompaniesRepository.getInstance().getAll();
    }
    public Company findCompanyById(int id) throws CompanyNotFoundException {
        return CompaniesRepository.getInstance().findById(id).orElseThrow(() -> new CompanyNotFoundException());
    }

    public List<User> getAllEmployeeByCompanyId(int id) throws CompanyNotFoundException {
        List<User> employees = new ArrayList<>();
        for(Integer empId : findCompanyById(id).getEmployeeIds()) {
            UsersRepository.getInstance().findById(empId).ifPresent(usr -> employees.add(usr));
        }
        return employees;
    }
}
