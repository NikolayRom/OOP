package command;

import model.IdGen;
import model.Identifiable;
import java.time.LocalDate;

public abstract class AbstractCommand implements Identifiable, Command {
    protected int id;
    protected int userId;
    protected LocalDate dateCreated;

    public AbstractCommand(int userId) {
        this.id = IdGen.getInstance().newId();
        this.userId = userId;
        this.dateCreated = LocalDate.now();
    }

    public abstract String toString();
    public abstract String execute() throws Exception;
    public abstract String undo() throws Exception;

    public int getUserId() {
        return this.userId;
    }
    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
