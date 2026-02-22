package command;

import java.math.BigDecimal;

import repository.CompaniesRepository;
import repository.UsersRepository;
import model.Client;
import exception.*;

public class CompanyDropCommand extends AbstractCommand {
    private int companyIdToRemoveFrom;
    private boolean hadSalaryProject;
    private BigDecimal oldSalary;

    public CompanyDropCommand(int userId, int companyIdToRemoveFrom) {
        super(userId);
        this.companyIdToRemoveFrom = companyIdToRemoveFrom;
    }

    public int getCompanyIdToRemoveFrom() {
        return this.companyIdToRemoveFrom;
    }
    public boolean getHadSalaryProject() {
        return this.hadSalaryProject;
    }
    public BigDecimal getOldSalary() {
        return this.oldSalary;
    }
    public void setHadSalaryProject(boolean bool) {
        this.hadSalaryProject = bool;
    }
    public void setOldSalary(BigDecimal amount) {
        this.oldSalary = amount;
    }

    @Override
    public String execute() throws DropFromCompanyException, ClientNotFoundException, CompanyNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).orElseThrow(() -> new ClientNotFoundException());

        if(client.getCompanyId() != getCompanyIdToRemoveFrom()) {
            throw new DropFromCompanyException();
        }
        setHadSalaryProject(client.getHasSalaryProject());
        setOldSalary(client.getSalary());
        CompaniesRepository.getInstance().findById(getCompanyIdToRemoveFrom()).ifPresentOrElse(c -> c.removeEmployee(getUserId()), () -> new CompanyNotFoundException());
        client.setCompanyId(-1);
        client.setDropSalaryProject();
        return "Выполнено: Клиент " + getUserId() + " уволен из компании " + getCompanyIdToRemoveFrom();
    }
    @Override
    public String undo() throws ClientNotFoundException, CompanyNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).orElseThrow(() -> new ClientNotFoundException());
        CompaniesRepository.getInstance().findById(getCompanyIdToRemoveFrom()).ifPresentOrElse(c -> c.addEmployee(getUserId()), () -> new CompanyNotFoundException());
        client.setCompanyId(getCompanyIdToRemoveFrom());
        if(getHadSalaryProject()) {
            client.setApplySalaryProject(getOldSalary());
        }
        return "Отмена: Увольнение клиента " + getUserId() + " отменено.";
    }
    @Override
    public String toString() {
        return "Command: Увольнение. Client: " + getUserId() + ", Company: " + getCompanyIdToRemoveFrom();
    }
}
