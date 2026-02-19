package model;

public final class IdGen {
    private static IdGen Instance;
    private int id = 0;
    private IdGen(){}
    public static IdGen getInstance() {
        if(Instance == null) {
            Instance = new IdGen();
        }
        return Instance;
    }

    public int newId() {
        return id++;
    }
}
