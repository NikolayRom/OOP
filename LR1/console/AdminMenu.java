package console;

import java.util.Scanner;
import service.AdminService;
import exception.*;
import model.User;
import java.util.List;
import java.util.ArrayList;

public class AdminMenu {
    private Scanner scanner = new Scanner(System.in);

    public AdminMenu() {
    }

    public void show() {
        while (true) {
            System.out.println("\n==========АДМИНИСТРАТОР==========");
            System.out.println("1. < Полный лог действий системы >");
            System.out.println("2. < Просмотр всех пользователей >");
            System.out.println("3. < Лог действий конкретного пользователя >");
            System.out.println("4. < Отменить последнее глобальное действие >");
            System.out.println("5. < Отменить последнее действие пользователя >");
            System.out.println("6. < Отмена всех глобальных действий >");
            System.out.println("0. < Выход >");
            System.out.print("Выбор: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        AdminService.getInstance().getFullSystemLog().forEach(res -> System.out.println(res));
                        break;
                    case "2":
                        showAllUsers().forEach(str -> System.out.println(str));
                        break;
                    case "3":
                        System.out.print("Id пользователя: ");
                        int uid = Integer.parseInt(scanner.nextLine());
                        AdminService.getInstance().getUserSystemLog(uid).forEach(res -> System.out.println(res));
                        break;
                    case "4":
                        System.out.println(AdminService.getInstance().undoLastGlobalAction());
                        break;
                    case "5":
                        System.out.print("Id пользователя: ");
                        int userId = Integer.parseInt(scanner.nextLine());
                        System.out.println(AdminService.getInstance().undoLastUserAction(userId));
                        break;
                    case "6":
                        AdminService.getInstance().resetSystem();
                        break;
                    case "0":
                        return;
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

    private List<String> showAllUsers() throws UserNotFoundException {
        List<User> list = AdminService.getInstance().getAllUsers();
        if(list.isEmpty() || list == null) {
            throw new UserNotFoundException();
        }
        List<String> result = new ArrayList<>();
        for(User user : list) {
            result.add(user.toString());
        }
        return result;
    }
}
