package console;

import java.util.Scanner;
import service.AdminService;
import exception.*;

public class AdminMenu {
    private Scanner scanner = new Scanner(System.in);

    public AdminMenu() {
    }

    public void show() {
        while (true) {
            System.out.println("\n==========АДМИНИСТРАТОР==========");
            System.out.println("1. < Полный лог действий системы >");
            System.out.println("2. < Лог действий конкретного пользователя >");
            System.out.println("3. < Отменить последнее глобальное действие >");
            System.out.println("4. < Отменить последнее действие пользователя >");
            System.out.println("5. < СБРОС СИСТЕМЫ >");
            System.out.println("0. < Выход >");
            System.out.print("Выбор: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        AdminService.getInstance().getFullSystemLog().forEach(res -> System.out.println(res));
                        break;
                    case "2":
                        System.out.print("Id пользователя: ");
                        int uid = Integer.parseInt(scanner.nextLine());
                        AdminService.getInstance().getUserSystemLog(uid).forEach(res -> System.out.println(res));
                        break;
                    case "3":
                        System.out.println(AdminService.getInstance().undoLastGlobalAction());
                        break;
                    case "4":
                        System.out.print("Id пользователя: ");
                        int userId = Integer.parseInt(scanner.nextLine());
                        System.out.println(AdminService.getInstance().undoLastUserAction(userId));
                        break;
                    case "5":
                        AdminService.getInstance().resetSystem();
                        break;
                    case "0":
                        return;
                    default:
                        throw new InvalidInputException();
                }
            } catch (Exception ex) {
                System.out.println("<<<ERROR: " + ex.getMessage() + ">>>");
            }
        }
    }
}
