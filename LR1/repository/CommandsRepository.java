package repository;
import command.AbstractCommand;

public class CommandsRepository extends MemoryManager<AbstractCommand> {
    private static CommandsRepository instance;
    private CommandsRepository() {}
    public static CommandsRepository getInstance() {
        if(instance == null) instance = new CommandsRepository();
        return instance;
    }
}
