package command;

import java.math.BigDecimal;
import model.Client;
import repository.UsersRepository;
import exception.*;

public class ApproveSalaryProjectCommand extends AbstractCommand {
    private BigDecimal salaryRate;
    private boolean hadSalaryProject;
    private BigDecimal oldSalary;

    public ApproveSalaryProjectCommand(int userId, BigDecimal salaryRate) {
        super(userId);
        this.salaryRate = salaryRate;
    }

    public BigDecimal getSalaryRate() {
        return this.salaryRate;
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
    public String execute() throws ClientNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).orElseThrow(() -> new ClientNotFoundException());
        setHadSalaryProject(client.getHasSalaryProject());
        setOldSalary(client.getSalary());
        client.setApplySalaryProject(getSalaryRate());
        return "Выполнено: Клиенту " + getUserId() + " одобрен зарплатный проект. Ставка: " + getSalaryRate();
    }
    @Override
    public String undo() throws ClientNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).orElseThrow(() -> new ClientNotFoundException());
        if(getHadSalaryProject()) {
            client.setApplySalaryProject(getOldSalary());
        }
        return "Отмена: Зарплатный проект для клиента " + getUserId() + " возвращен к прежнему состоянию.";
    }
    @Override
    public String toString() {
        return "Command: Одобрение зарплатного проекта. Client: " + getUserId() + ", Rate: " + getSalaryRate();
    }
}