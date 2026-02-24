package model;

import java.util.List;
import java.util.ArrayList;

public class Company implements Identifiable {
    private int id;
    private String name;
    private CompanyType type;
    private List<Integer> employeeIds;

    public Company(String name, CompanyType type) {
        this.id = IdGen.getInstance().newId();
        this.name = name;
        this.type = type;
        this.employeeIds = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }
    public CompanyType getType() {
        return this.type;
    }
    public List<Integer> getEmployeeIds() {
        return new ArrayList<>(employeeIds);
    }

    public void addEmployee(int employeeId) {
        if(!employeeIds.contains(employeeId)) {
            employeeIds.add(employeeId);
        }
    }
    public void removeEmployee(int employeeId) {
        employeeIds.remove((Integer) employeeId);
    }

    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public String toString() {
        return String.format("Company: %s (%s), Сотрудников: %d", getName(), getType(), getEmployeeIds().size());
    }
}
