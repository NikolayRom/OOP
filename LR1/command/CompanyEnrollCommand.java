package command;

import java.math.BigDecimal;

import exception.ClientNotFoundException;
import exception.CompanyNotFoundException;
import model.Client;
import repository.CompaniesRepository;
import repository.UsersRepository;

public class CompanyEnrollCommand extends AbstractCommand {
    private int targetCompanyId;
    private int oldCompanyId;
    private boolean hadSalaryProject;
    private BigDecimal oldSalary;

    public CompanyEnrollCommand(int userId, int targetCompanyId) {
        super(userId);
        this.targetCompanyId = targetCompanyId;
    }

    public int getTargetCompanyId() {
        return this.targetCompanyId;
    }
    public int getOldCompanyId() {
        return this.oldCompanyId;
    }
    public boolean getHadSalaryProject() {
        return this.hadSalaryProject;
    }
    public BigDecimal getOldSalary() {
        return this.oldSalary;
    }
    public void setOldCompanyId(int id) {
        this.oldCompanyId = id;
    }
    public void setHadSalaryProject(boolean bool) {
        this.hadSalaryProject = bool;
    }
    public void setOldSalary(BigDecimal amount) {
        this.oldSalary = amount;
    }

    @Override
    public String execute() throws ClientNotFoundException, CompanyNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).orElseThrow(() -> new ClientNotFoundException());
        setOldCompanyId(client.getCompanyId());
        setOldSalary(client.getSalary());
        setHadSalaryProject(client.getHasSalaryProject());

        if(getOldCompanyId() > 0) {
            CompaniesRepository.getInstance().findById(getOldCompanyId()).ifPresentOrElse(c -> c.removeEmployee(getUserId()), () -> new CompanyNotFoundException());
        }
        CompaniesRepository.getInstance().findById(getTargetCompanyId()).ifPresentOrElse(c -> c.addEmployee(getUserId()), () -> new CompanyNotFoundException());
        client.setDropSalaryProject();
        client.setCompanyId(getTargetCompanyId());
        return "Выполнено: Клиент " + getUserId() + " принят в компанию " + getTargetCompanyId();
    }

    @Override
    public String undo() throws ClientNotFoundException, CompanyNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).orElseThrow(() -> new ClientNotFoundException());
        CompaniesRepository.getInstance().findById(getTargetCompanyId()).ifPresentOrElse(c -> c.removeEmployee(getUserId()), () -> new CompanyNotFoundException());
        if(getOldCompanyId() > 0) {
            CompaniesRepository.getInstance().findById(getOldCompanyId()).ifPresentOrElse(c -> c.addEmployee(getUserId()), () -> new CompanyNotFoundException());
        }
        if(getHadSalaryProject()) {
            client.setApplySalaryProject(getOldSalary());
        }
        client.setCompanyId(getOldCompanyId());
        return "Отмена: Клиент " + getUserId() + " возвращен на прежнее место работы.";
    }

    @Override
    public String toString() {
        return "Command: Прием на работу. Client: " + getUserId() + ", TargetCompany: " + getTargetCompanyId();
    }
}
