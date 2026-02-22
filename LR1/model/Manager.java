package model;

public class Manager extends User {
    public Manager(String login, String password, String name) {
        super(login, password, name, Role.MANAGER);
    }
}
