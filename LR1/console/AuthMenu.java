package console;

import java.nio.file.AccessDeniedException;
import java.util.Scanner;

import exception.InvalidLoginException;
import exception.LoginExistingException;
import exception.RequestPendingException;
import exception.InvalidInputException;
import model.User;
import service.AuthService;

public class AuthMenu {
    private Scanner scanner = new Scanner(System.in);

    public User show() {
        while(true) {
            System.out.println("\n\n\n==========ВХОД==========");
            System.out.println("1. < Войти >");
            System.out.println("2. < Зарегестрироваться >");
            System.out.println("0. < Выход >");

            String choice = scanner.nextLine();
            try {
                switch(choice) {
                    case "1":
                        User user = loginProcess();
                        if(user != null) return user;
                        break;
                    case "2":
                        registrationProcess();
                        break;
                    case "0":
                        System.exit(0);
                        break;
                    default:
                        throw new InvalidInputException();
                }
            } catch(NumberFormatException ex) {
                System.out.println("<<<ERROR: Invalid input: некорректный ввод для команды, попробуйте еще раз>>>");
            } catch (Exception ex) {
                System.out.println("<<<ERROR: " + ex.getMessage() + ">>>");
            }
        }
    }

    private User loginProcess() throws InvalidLoginException, AccessDeniedException, RequestPendingException, Exception {
        System.out.println("Введите логин: ");
        String login = scanner.nextLine();
        System.out.println("Введите пароль: ");
        String password = scanner.nextLine();
        return AuthService.getInstance().login(login, password);
    }
    private String registrationProcess() throws LoginExistingException {
        System.out.println("Введите новый логин: ");
        String login = scanner.nextLine();
        System.out.println("Введите новый пароль: ");
        String password = scanner.nextLine();
        System.out.println("Введите новое имя: ");
        String name = scanner.nextLine();
        return AuthService.getInstance().registerClient(login, password, name);
    }
}
