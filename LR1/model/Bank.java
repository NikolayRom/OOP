package model;

public abstract class Bank implements Identifiable {
    private int id;
    private String name;

    public Bank(String name) {
        this.id = IdGen.getInstance().newId();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public String toString() {
        return "Bank{id=" + getId() + ", name='" + getName() + "'}";
    }
}
