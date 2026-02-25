package model;

import java.math.BigDecimal;

public class Client extends User {
    private ClientStatus status;
    private int companyId;
    private boolean hasSalaryProject;
    private BigDecimal salary;

    public Client(String login, String password, String name) {
        super(login, password, name, Role.CLIENT);
        this.status = ClientStatus.PENDING;
        this.companyId = -1;
        this.hasSalaryProject = false;
        this.salary = BigDecimal.ZERO;
    }


    public ClientStatus getStatus() {
        return this.status;
    }
    public int getCompanyId() {
        return this.companyId;
    }
    public boolean getHasSalaryProject() {
        return this.hasSalaryProject;
    }
    public BigDecimal getSalary() {
        return this.salary;
    }
    public void setStatus(ClientStatus status) {
        this.status = status;
    }
    public void setCompanyId(int id) {
        this.companyId = id;
    }
    public void setApplySalaryProject(BigDecimal salary) {
        if(salary.compareTo(BigDecimal.ZERO) <= 0) {
            setDropSalaryProject();
        }
        this.hasSalaryProject = true;
        this.salary = salary;
    }
    public void setDropSalaryProject() {
        this.hasSalaryProject = false;
        this.salary = BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "Client{" + getId() + ", name='" + getName() + "', status=" + getStatus() + ", hasSalaryProject='" + getHasSalaryProject() + '}';
    }
}
