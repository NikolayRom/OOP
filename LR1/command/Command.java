package command;

public interface Command {
    String execute() throws Exception;
    String undo() throws Exception;
    String toString();
}
