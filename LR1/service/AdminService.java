package service;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Map;

import command.AbstractCommand;
import repository.CommandsRepository;
import repository.UsersRepository;
import exception.*;
import model.Role;
import model.User;

import java.util.Stack;

public class AdminService {
    private static AdminService instance;
    private AdminService() {}
    public static AdminService getInstance() {
        if(instance == null) {
            instance = new AdminService();
        }
        return instance;
    }

    public List<String> getFullSystemLog() {
        List<AbstractCommand> commands = new ArrayList<>();
        for(Stack<AbstractCommand> stack : CommandsRepository.getInstance().getAllLogs().values()) {
            commands.addAll(stack);
        }
        commands.sort(Comparator.comparingInt(cmd -> cmd.getId()));
        return commands.stream().map(cmd -> String.format("[Id: %d] User: %s | Action: %s", cmd.getId(), UsersRepository.getInstance().findById(cmd.getUserId()).map(usr -> usr.getLogin()).orElse("Unknow user. Id: " + cmd.getUserId()), cmd.getClass().getSimpleName())).toList();
    }
    public List<User> getAllUsers() {
        return UsersRepository.getInstance().getAll().stream().filter(usr -> usr.getRole() != Role.ADMIN).toList();
    }

    public List<String> getUserSystemLog(int userId) throws UserNotFoundException {
        validateUser(userId);
        List<AbstractCommand> commands = new ArrayList<>();
        commands.addAll(CommandsRepository.getInstance().getUserLogs(userId));
        if(commands == null || commands.isEmpty()) {
            return new ArrayList<>();
        }
        return commands.stream().map(cmd -> String.format("[Id: %d] Action: %s", cmd.getId(), cmd.getClass().getSimpleName())).toList();
    }

    public String undoLastGlobalAction() throws UserNotFoundException {
        int maxId = -1;
        Integer userId = null;

        for(Map.Entry<Integer, Stack<AbstractCommand>> entry : CommandsRepository.getInstance().getAllLogs().entrySet()) {
            Stack<AbstractCommand> stack = entry.getValue();
            if(!stack.isEmpty()) {
                AbstractCommand topCmd = stack.peek();
                if(topCmd.getId() > maxId) {
                    maxId = topCmd.getId();
                    userId = entry.getKey();
                }
            }
        }

        if(userId != null) {
            return undoLastUserAction(userId);
        } else {
            return getUndoLastGlobalActionNotFoundMessage();
        }
    }

    public String undoLastUserAction(int userId) throws UserNotFoundException {
        validateUser(userId);
        Optional<AbstractCommand> command = CommandsRepository.getInstance().pop(userId);
        if(command.isPresent()) {
            AbstractCommand cmd = command.get();
            try {
                return cmd.undo();
            } catch(Exception ex) {
                return "Ошибка: " + cmd.getId() + " : " + ex.getMessage(); 
            }
        } else {
            return "У пользователя " + userId + " нет действий для отмены.";
        }
    }

    public List<String> resetSystem() throws UserNotFoundException {
        List<String> logs = new ArrayList<>();
        while(true) {
            String result = undoLastGlobalAction();
            if(result.contains(getUndoLastGlobalActionNotFoundMessage())) {
                break;
            }
            logs.add(result);
        }
        return logs;
    }

    private String getUndoLastGlobalActionNotFoundMessage() {
        return "История действий пуста, нечего отменять";
    }
    private void validateUser(int userId) throws UserNotFoundException {
        if(UsersRepository.getInstance().findById(userId).orElseThrow(() -> new UserNotFoundException()).getRole().equals(Role.ADMIN)) {
            throw new UserNotFoundException();
        }
    }
}
