package model;

public class Admin extends User {
    public Admin(String login, String password, String name) {
        super(login, password, name, Role.ADMIN);
    }
}
