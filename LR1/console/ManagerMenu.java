package console;

import exception.*;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import service.ManagerService;
import service.ClientService;
import service.CompanyService;
import model.Request;
import model.Transaction;
import model.Account;

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
            System.out.println("5. < Просмотр всех компаний > ");
            System.out.println("6. < Просмотр всех клиентов >");
            System.out.println("7. < История транзакций клиента >");
            System.out.println("8. < Просмотр всех счетов/вкладов клиента >");
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
                    case "5":
                        CompanyService.getInstance().getAllCompanies().forEach(company -> System.out.println("> " + company.getId() + ". " + company.getName() + ";"));
                        break;
                    case "6":
                        showAllClients().forEach(str -> System.out.println(str));
                        break;
                    case "7":
                        System.out.println("Id пользователя:");
                        showTransactions(Integer.parseInt(scanner.nextLine())).forEach(str -> System.out.println(str));;
                        break;
                    case "8":
                        System.out.println("Id пользователя:");
                        showAllAccountsByClient(Integer.parseInt(scanner.nextLine())).forEach(str -> System.out.println(str));
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

    private void processRequests() throws NoRequestException {
        Request req = ManagerService.getInstance().getNextRequestInfo();
        if(req == null) {
            throw new NoRequestException();
        }

        System.out.println("\n==========ТЕКУЩИЙ ЗАПРОС==========");
        System.out.println(req.toString());
        for(String key: req.getListParams().keySet()) {
            System.out.println("[" + key + "] : " + req.getListParams().get(key));
        }

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
        } catch(NumberFormatException ex) {
                System.out.println("<<<ERROR: Invalid input: некорректный ввод для команды, попробуйте еще раз>>>");
        } catch (Exception ex) {
            System.out.println("<<<ERROR: " + ex.getMessage() + ">>>");
        }
    }
    private List<String> showTransactions(int userId) throws TransactionNotFoundException, ClientNotFoundException {
        List<Transaction> list = ClientService.getInstance().getClientTransactions(userId);
        if(list.isEmpty() || list == null) {
            throw new TransactionNotFoundException();
        }
        List<String> result = new ArrayList<>();
        for(Transaction trans : list) {
            result.add(trans.toString());
        }
        return result;
    }
    private List<String> showAllClients() throws ClientNotFoundException {
        List<User> list = ManagerService.getInstance().getAllClients();
        if(list.isEmpty() || list == null) {
            throw new ClientNotFoundException();
        }
        List<String> result = new ArrayList<>();
        for(User user : list) {
            result.add(user.toString());
        }
        return result;
    }
    private List<String> showAllAccountsByClient(int userId) throws AccountNotFoundException, ClientNotFoundException {
        List<Account> list = ClientService.getInstance().getClientAccounts(userId);
        if(list.isEmpty() || list == null) {
            throw new AccountNotFoundException();
        }
        List<String> result = new ArrayList<>();
        for(Account account : list) {
            result.add(account.toString());
        }
        return result;
    }
}
