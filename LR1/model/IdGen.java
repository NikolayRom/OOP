package model;

public final class IdGen {
    private static IdGen instance;
    private int id = 1;
    private IdGen(){}
    public static IdGen getInstance() {
        if(instance == null) {
            instance = new IdGen();
        }
        return instance;
    }

    public int newId() {
        return id++;
    }
}
