package console;

import model.User;
import service.ClientService;
import model.Account;
import model.AccountType;
import model.Transaction;

import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

import exception.*;

public class ClientMenu {
    private User user;
    private Scanner scanner = new Scanner(System.in);

    public ClientMenu(User user) {
        this.user = user;
    }

    public void show() {
        
        while(true) {
            System.out.println("\n==========КЛИЕНТ==========");
            System.out.println("1. < Мои счета >");
            System.out.println("2. < История транзакций >");
            System.out.println("3. < Перевод средств >");
            System.out.println("4. < Открыть новый счет/вклад >");
            System.out.println("5. < Закрыть счет/вклад >");
            System.out.println("6. < Накопление вклада >");
            System.out.println("7. < Предприятия и зарплатный проект >");
            System.out.println("0. < Выход в главное меню >");
        
            String choice = scanner.nextLine();
        
            try {
                switch(choice) {
                    case "1":
                        for(String res : showAccounts()) {
                            System.out.println("\n> " + res + ";");
                        }
                        break;
                    case "2":
                        for(String res : showTransactions()) {
                            System.out.println("\n> " + res + ";");
                        }
                        break;
                    case "3":
                        System.out.println(makeTransfer());
                        break;
                    case "4":
                        System.out.println(openAccount());
                        break;
                    case "5":
                        System.out.println(closeAccount());
                        break;
                    case "6":
                        System.out.println(doAccumulation());
                        break;
                    case "7":
                        companyMenu();
                        break;
                    case "0":
                        return;
                    default:
                        throw new InvalidInputException();
                }
            } catch(Exception ex) {
                System.out.println("<<<ERROR: " + ex.getMessage() + ">>>");
            }
        }
    }

    private List<String> showAccounts() throws ClientNotFoundException, AccountNotFoundException {
        List<Account> list = ClientService.getInstance().getClientAccounts(user.getId());
        if(list.isEmpty() || list == null) {
            throw new AccountNotFoundException();
        }
        List<String> result = new ArrayList<>();
        for(Account account : list) {
            result.add(account.toString());
        }
        return result;
    }
    private List<String> showTransactions() throws TransactionNotFoundException, ClientNotFoundException {
        List<Transaction> list = ClientService.getInstance().getClientTransactions(user.getId());
        if(list.isEmpty() || list == null) {
            throw new TransactionNotFoundException();
        }
        List<String> result = new ArrayList<>();
        for(Transaction trans : list) {
            result.add(trans.toString());
        }
        return result;
    }
    private String makeTransfer() throws ClientNotFoundException, AccountNotFoundException, Exception {
        System.out.println("Id счета списания:");
        int fromId = Integer.parseInt(scanner.nextLine());
        System.out.println("Id счета получателя:");
        int toId = Integer.parseInt(scanner.nextLine());
        System.out.println("Сумма:");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        return ClientService.getInstance().transferFunds(user.getId(), fromId, toId, amount);
    }
    private String openAccount() throws ClientNotFoundException, BankNotFoundException, InvalidInterestRateException, InvalidDurationInMonthException {
        System.out.println("Доступные для открытия счета/вклада банки:");
        ClientService.getInstance().getAllBanks().forEach(bank -> System.out.println("> " + bank.getId() + ". " + bank.getName() + ";"));

        System.out.println("Введите id банка:");
        int bankId = Integer.parseInt(scanner.nextLine());

        while(true) {
            System.out.println("Введите тип счета (1 -  Дебетовый, 2 - Вклад):");
            String typeChoice = scanner.nextLine();
            switch(typeChoice) {
                case "1":
                    return ClientService.getInstance().requestAccountOpening(user.getId(), bankId, AccountType.DEBIT);
                case "2":
                    System.out.println("Введите срок действия (в месяцах):");
                    int durationInMonth = Integer.parseInt(scanner.nextLine());
                    System.out.println("Введите процентную ставку (0.05 = 5%):");
                    BigDecimal interestRate = new BigDecimal(scanner.nextLine());
                    return ClientService.getInstance().requestAccountOpening(user.getId(), bankId, AccountType.DEPOSIT, interestRate, durationInMonth);
                default:
                    System.out.println("Некорректный ввод для типа счета, попробуйте еще раз");
                    break;
            }
        }
    }
    private String closeAccount() throws ClientNotFoundException, AccountNotFoundException, Exception {
        System.out.println("Введите Id счета/вклада для закрытия: ");
        int id = Integer.parseInt(scanner.nextLine());
        return ClientService.getInstance().requestAccountRemoving(user.getId(), id);
    }
    private String doAccumulation() throws ClientNotFoundException, AccountNotFoundException, AccountNotDepositException, EndDepositDurationException, InsufficientFundsException, Exception {
        System.out.println("Введите Id вклада для начисления процентов: ");
        int id = Integer.parseInt(scanner.nextLine());
        return ClientService.getInstance().accumulateDepositAccount(user.getId(), id);

    }
    private void companyMenu() {
        while (true) {
            System.out.println("\n==========ПРЕДПРИЯТИЯ И ЗАРПЛАТНЫЙ ПРОЕКТ==========");
            System.out.println("1. < Список компаний >");
            System.out.println("2. < Подать заявку на трудоустройство >");
            System.out.println("3. < Подать заявку на Зарплатный Проект >");
            System.out.println("4. < Получить зарплату >");
            System.out.println("0. < Назад >");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        ClientService.getInstance().getAllCompanies().forEach(company -> System.out.println("> " + company.getId() + ". " + company.getName() + ";"));
                        break;
                    case "2":
                        System.out.println("Id компании: ");
                        int cId = Integer.parseInt(scanner.nextLine());
                        System.out.println(ClientService.getInstance().requestCompanyEnroll(user.getId(), cId));
                        break;
                    case "3":
                        System.out.println("Id компании: ");
                        int compId = Integer.parseInt(scanner.nextLine());
                        System.out.println("Желаемая зарплата: ");
                        BigDecimal salary = new BigDecimal(scanner.nextLine());
                        System.out.println(ClientService.getInstance().requestApproveSalaryProject(user.getId(), compId, salary));
                        break;
                    case "4":
                        System.out.println("Id счета для зачисления: ");
                        int accId = Integer.parseInt(scanner.nextLine());
                        System.out.println("Сумма к выплате: ");
                        BigDecimal am = new BigDecimal(scanner.nextLine());
                        System.out.println(ClientService.getInstance().requestSalaryPayment(user.getId(), accId, am));
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
