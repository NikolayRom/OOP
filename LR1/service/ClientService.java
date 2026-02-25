package service;

import repository.AccountsRepository;
import repository.BanksRepository;
import repository.CommandsRepository;
import repository.CompaniesRepository;
import repository.RequestsRepository;
import repository.TransactionsRepository;
import repository.UsersRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import command.TransactionCommand;
import exception.*;

import model.Bank;
import model.Company;
import model.DepositAccount;
import model.RequestType;
import model.Role;
import model.Account;
import model.AccountType;
import model.Request;
import model.Transaction;

public class ClientService {
    private static ClientService instance;
    private ClientService() {}
    public static ClientService getInstance() {
        if(instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public List<Bank> getAllBanks() {
        return BanksRepository.getInstance().getAll();
    }
    public List<Company> getAllCompanies() {
        return CompaniesRepository.getInstance().getAll();
    }
    public List<Account> getClientAccounts(int clientId) throws ClientNotFoundException {
        validateClient(clientId);
        return AccountsRepository.getInstance().getAll().stream().filter(account -> account.getUserId() == clientId).toList();
    }
    public List<Transaction> getClientTransactions(int clientId) throws ClientNotFoundException {
        List<Account> accountList = getClientAccounts(clientId);
        List<Transaction> transactionList = new ArrayList<>();
        for(Account account : accountList) {
            transactionList.addAll(TransactionsRepository.getInstance().getByAccountId(account.getId()));
        }
        return transactionList;
    }

    public String requestAccountOpening(int clientId, int bankId, AccountType accountType, BigDecimal interestRate, int durationInMonth) throws ClientNotFoundException, BankNotFoundException, InvalidDurationInMonthException, InvalidInterestRateException {
        validateClient(clientId);
        validateDurationInMonth(durationInMonth);
        validateInterestRate(interestRate);
        BanksRepository.getInstance().findById(bankId).orElseThrow(() -> new BankNotFoundException());
        Request request = new Request(RequestType.OPEN_ACCOUNT, clientId);
        request.addParam("bankId", String.valueOf(bankId));
        request.addParam("accountType", accountType.toString());

        if(accountType == AccountType.DEPOSIT) {
            request.addParam("interestRate", interestRate.toString());
            request.addParam("duration", String.valueOf(durationInMonth));
        }
        RequestsRepository.getInstance().push(request);
        return "Заявка на открытие счета отправлена менеджеру.";
    }
    public String requestAccountOpening(int clientId, int bankId, AccountType accountType) throws ClientNotFoundException, BankNotFoundException {
        validateClient(clientId);
        BanksRepository.getInstance().findById(bankId).orElseThrow(() -> new BankNotFoundException());
        Request request = new Request(RequestType.OPEN_ACCOUNT, clientId);
        request.addParam("bankId", String.valueOf(bankId));
        request.addParam("accountType", accountType.toString());

        RequestsRepository.getInstance().push(request);
        return "Заявка на открытие счета отправлена менеджеру.";
    }

    public String requestAccountRemoving(int clientId, int accountId) throws AccountNotFoundException, ClientNotFoundException {
        validateClient(clientId);
        validateAccount(clientId, accountId);
        Request request = new Request(RequestType.REMOVE_ACCOUNT, clientId);
        request.addParam("accountId", String.valueOf(accountId));
        RequestsRepository.getInstance().push(request);
        
        return "Заявка на закрытие счета отправлена.";
    }

    public String requestCompanyEnroll(int clientId, int companyId) throws CompanyNotFoundException, ClientNotFoundException {
        validateClient(clientId);
        CompaniesRepository.getInstance().findById(companyId).orElseThrow(() -> new CompanyNotFoundException());
        Request request = new Request(RequestType.ENROLL_COMPANY, clientId);
        request.addParam("companyId", String.valueOf(companyId));
        RequestsRepository.getInstance().push(request);
        return "Заявка на регистрацию в компанию отправлена.";
    }

    public String requestCompanyDrop(int clientId, int companyId) throws CompanyNotFoundException, ClientNotFoundException {
        validateClient(clientId);
        CompaniesRepository.getInstance().findById(companyId).orElseThrow(() -> new CompanyNotFoundException());
        Request request = new Request(RequestType.DROP_COMPANY, clientId);
        request.addParam("companyId", String.valueOf(companyId));
        RequestsRepository.getInstance().push(request);
        return "Заявка на увольнение из компании отправлена";
    }

    public String requestApproveSalaryProject(int clientId, int companyId, BigDecimal salary) throws ClientNotFoundException, CompanyNotFoundException, ClientNotEmployeeException, InvalidAmountInputException {
        validateClient(clientId);
        validateAmount(salary);
        CompaniesRepository.getInstance().findById(companyId).orElseThrow(() -> new CompanyNotFoundException());
        if (!CompaniesRepository.getInstance().findById(companyId).orElseThrow(() -> new CompanyNotFoundException()).getEmployeeIds().contains(clientId)) {
            throw new ClientNotEmployeeException();
        }
        Request request = new Request(RequestType.SALARY_PROJECT, clientId);
        request.addParam("amount", salary.toString());
        RequestsRepository.getInstance().push(request);
        return "Заявка на подтверждение зарплатного проекта отправлена.";
    }

    public String requestSalaryPayment(int clientId, int accountId, BigDecimal salary) throws ClientNotFoundException, AccountNotFoundException, InvalidAmountInputException {
        validateClient(clientId);
        validateAccount(clientId, accountId);
        validateAmount(salary);
        Request request = new Request(RequestType.SALARY, clientId);
        request.addParam("accountId", String.valueOf(accountId));
        request.addParam("amount", salary.toString());
        RequestsRepository.getInstance().push(request);
        
        return "Заявка на выплату зарплаты отправлена.";
    }

    public String transferFunds(int clientId, int fromAccountId, int toAccountId, BigDecimal amount) throws ClientNotFoundException, AccountNotFoundException, InvalidAmountInputException, Exception {
        validateClient(clientId);
        validateAccount(clientId, fromAccountId);
        validateAccount(clientId, toAccountId);
        validateAmount(amount);
        TransactionCommand command = new TransactionCommand(clientId, fromAccountId, toAccountId, amount);
        command.execute();
        CommandsRepository.getInstance().push(clientId, command);
        
        return "Успешный перевод средств с " + fromAccountId + " на " + toAccountId;
    }

    public String accumulateDepositAccount(int clientId, int accountId) throws ClientNotFoundException, AccountNotFoundException, AccountNotDepositException, EndDepositDurationException, InsufficientFundsException {
        validateClient(clientId);
        validateAccount(clientId, accountId);
        if(!AccountsRepository.getInstance().findById(accountId).orElseThrow(() -> new AccountNotFoundException()).getType().equals(AccountType.DEPOSIT)) {
            throw new AccountNotDepositException();
        }
        DepositAccount account = (DepositAccount) AccountsRepository.getInstance().findById(accountId).orElseThrow(() -> new AccountNotFoundException());
        account.accumulation();
        return "Операция <накопление> была успешно завершена на " + accountId;
    }

    private void validateClient(int clientId) throws ClientNotFoundException {
        UsersRepository.getInstance().findById(clientId).filter(usr -> usr.getRole() == Role.CLIENT).orElseThrow(() -> new ClientNotFoundException());
    }
    private void validateAccount(int clientId, int accountId) throws AccountNotFoundException {
        if(AccountsRepository.getInstance().findById(accountId).orElseThrow(() -> new AccountNotFoundException()).getUserId() != clientId) {
            throw new AccountNotFoundException();
        }
    }
    private void validateInterestRate(BigDecimal interestRate) throws InvalidInterestRateException {
        if(interestRate.compareTo(BigDecimal.ZERO) < 0 || interestRate.compareTo(BigDecimal.ONE) > 0) {
            throw new InvalidInterestRateException();
        }
    }
    private void validateDurationInMonth(int durationInMonth) throws InvalidDurationInMonthException {
        if(durationInMonth < 1) {
            throw new InvalidDurationInMonthException();
        }
    }
    private void validateAmount(BigDecimal amount) throws InvalidAmountInputException {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountInputException();
        }
    }
}
