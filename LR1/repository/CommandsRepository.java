package repository;

import command.AbstractCommand;
import java.util.HashMap;
import java.util.Stack;
import java.util.Optional;

public class CommandsRepository {
    private static CommandsRepository instance;
    private HashMap<Integer, Stack<AbstractCommand>> logs = new HashMap<>();
    private CommandsRepository() {
        this.logs = new HashMap<>();
    }
    public static CommandsRepository getInstance() {
        if(instance == null) instance = new CommandsRepository();
        return instance;
    }

    public void push(int userId, AbstractCommand command) {
        if(!logs.containsKey(userId)) {
            logs.put(userId, new Stack<>());
        }
        logs.get(userId).push(command);
    }

    public Optional<AbstractCommand> pop(int userId) {
        if(!logs.containsKey(userId) || logs.get(userId) == null || logs.get(userId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(logs.get(userId).pop());
    }

    public Stack<AbstractCommand> getUserLogs(int userId) {
        return logs.get(userId);
    }

    public HashMap<Integer, Stack<AbstractCommand>> getAllLogs() {
        return logs;
    }
}
