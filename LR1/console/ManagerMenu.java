package console;

import exception.*;
import model.User;
import java.util.Scanner;
import service.ManagerService;
import model.Request;

public class ManagerMenu {
    private User user;
    private Scanner scanner = new Scanner(System.in);

    public ManagerMenu(User user) {
        this.user = user;
    }

    public void show() {
        while (true) {
            System.out.println("\n==========МЕНЕДЖЕР==========");
            System.out.println("1. < Обработать следующую заявку >");
            System.out.println("2. < Блокировать счет клиента >");
            System.out.println("3. < Разблокировать счет клиента >");
            System.out.println("4. < Просмотр сотрудников компании >");
            System.out.println("0. < Выход >");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": 
                        processRequests();
                        break;
                    case "2": 
                        System.out.println("Id счета: ");
                        ManagerService.getInstance().blockClientAccount(user.getId(), Integer.parseInt(scanner.nextLine()));
                        System.out.println("Счет заблокирован.");
                        break;
                    case "3":
                        System.out.print("Id счета: ");
                        ManagerService.getInstance().unblockClientAccount(user.getId(), Integer.parseInt(scanner.nextLine()));
                        System.out.println("Счет разблокирован.");
                        break;
                    case "4":
                        System.out.print("Id компании: ");
                        ManagerService.getInstance().getCompanyEmployees(Integer.parseInt(scanner.nextLine())).forEach(u -> System.out.println(u.toString()));
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

    private void processRequests() throws NoRequestException {
        Request req = ManagerService.getInstance().getNextRequestInfo();
        if(req == null) {
            throw new NoRequestException();
        }

        System.out.println("\n==========ТЕКУЩИЙ ЗАПРОС==========");
        System.out.println(req.toString());
        System.out.println("Параметры: " + req.getDetails() + ";\n");

        System.out.println("1. ОДОБРИТЬ");
        System.out.println("2. ОТКЛОНИТЬ");
        
        String decision = scanner.nextLine();

        try {
            switch(decision) {
                case "1":
                    System.out.println(ManagerService.getInstance().processNextRequest(user.getId(), true));
                    break;
                case "2":
                    System.out.println(ManagerService.getInstance().processNextRequest(user.getId(), false));
                    break;
                default:
                    throw new InvalidInputException();
            }
        } catch (Exception ex) {
            System.out.println("<<<ERROR: " + ex.getMessage() + ">>>");
        }
    }
}
