package model;

public abstract class User implements Identifiable {
    protected String login;
    protected String password;
    protected String name;
    protected int id;
    protected Role role;

    User(String login, String password, String name, Role role) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.id = IdGen.getInstance().newId();
        this.role = role;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "> Id: " + getId() + " | Role: " + getRole() + " | Login: " + getLogin() + " | Name: " + getName() + ";";
    }

    public String getLogin() {
        return this.login;
    }
    public String getPassword() {
        return this.password;
    }
    public String getName() {
        return this.name;
    }
    public Role getRole() {
        return this.role;
    }
}
