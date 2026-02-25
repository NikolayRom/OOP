package service;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import command.*;
import model.Request;
import model.RequestStatus;
import model.Role;
import exception.*;
import model.AccountType;
import java.math.BigDecimal;
import repository.*;
import model.User;
import model.Transaction;

public class ManagerService {
    private static ManagerService instance;
    private ManagerService() {}
    public static ManagerService getInstance() {
        if(instance == null) {
            instance = new ManagerService();
        }
        return instance;
    }

    public List<Request> getPendingRequest() {
        return RequestsRepository.getInstance().getAll().stream().filter(req -> req.getStatus().equals(RequestStatus.PENDING)).toList();
    }
    public List<User> getAllClients() {
        return UsersRepository.getInstance().getAll().stream().filter(usr -> usr.getRole() == Role.CLIENT).toList();
    }
    
    public Request getNextRequestInfo() {
        return RequestsRepository.getInstance().peek().orElse(null);
    }

    public String processNextRequest(int managerId, boolean approve) throws NoRequestException, Exception {
        validateManager(managerId);
        Request request = RequestsRepository.getInstance().pop().orElseThrow(() -> new NoRequestException());
        
        if(!approve) {
            request.setRejected();
            return "Заявка " + request.getId() + " отклонена.";
        }

        AbstractCommand command = null;

        switch(request.getType()) {
            case REGISTRATION:
                command = new RegistrationCommand(request.getUserId());
                break;
            case SALARY:
                int accountId = Integer.parseInt(request.getParam("accountId"));
                BigDecimal amount = new BigDecimal(request.getParam("amount"));
                command = new PaySalaryCommand(request.getUserId(), accountId, amount);
                break;
            case OPEN_ACCOUNT:
                int bankId = Integer.parseInt(request.getParam("bankId"));
                AccountType type = AccountType.valueOf(request.getParam("accountType"));
                if(type == AccountType.DEPOSIT) {
                    BigDecimal interestRate = new BigDecimal(request.getParam("interestRate"));
                    int duration = Integer.parseInt(request.getParam("duration"));
                    command = new OpenAccountCommand(request.getUserId(), bankId, type, interestRate, duration);
                    break;
                }
                command = new OpenAccountCommand(request.getUserId(), bankId, type);
                break;
            case REMOVE_ACCOUNT:
                int accId = Integer.parseInt(request.getParam("accountId"));
                command = new RemoveAccountCommand(request.getUserId(), accId);
                break;
            case ENROLL_COMPANY:
                int companyId = Integer.parseInt(request.getParam("companyId"));
                command = new CompanyEnrollCommand(request.getUserId(), companyId);
                break;
            case DROP_COMPANY:
                int compId = Integer.parseInt(request.getParam("companyId"));
                command = new CompanyDropCommand(request.getUserId(), compId);
                break;
            case SALARY_PROJECT:
                BigDecimal salary = new BigDecimal(request.getParam("amount"));
                command = new ApproveSalaryProjectCommand(request.getUserId(), salary);
                break;
        }
        if(command != null) {
            command.execute();
            CommandsRepository.getInstance().push(managerId, command);
            request.setApproved();
            return "Заявка " + request.getId() + " (" + request.getType() + ") успешно одобрена.";
        }
        return "Ошибка обработки запроса";
    }

    public void blockClientAccount(int managerId, int accountId) throws AccountNotFoundException, Exception {
        validateManager(managerId);
        BlockAccountCommand command = new BlockAccountCommand(AccountsRepository.getInstance().findById(accountId).orElseThrow(() -> new AccountNotFoundException()).getUserId(), accountId);
        command.execute();
        CommandsRepository.getInstance().push(managerId, command);
    }
    public void unblockClientAccount(int managerId, int accountId) throws AccountNotFoundException, Exception {
        validateManager(managerId);
        UnblockAccountCommand command = new UnblockAccountCommand(AccountsRepository.getInstance().findById(accountId).orElseThrow(() -> new AccountNotFoundException()).getUserId(), accountId);
        command.execute();
        CommandsRepository.getInstance().push(managerId, command);
    }
    public List<User> getCompanyEmployees(int companyId) throws CompanyNotFoundException {
        List<Integer> empIds = CompaniesRepository.getInstance().findById(companyId).orElseThrow(() -> new CompanyNotFoundException()).getEmployeeIds();
        return UsersRepository.getInstance().getAll().stream().filter(usr -> empIds.contains(usr.getId())).toList();
    }
    public List<Transaction> getClientAccountHistory(int accountId) throws AccountNotFoundException {
        AccountsRepository.getInstance().findById(accountId).orElseThrow(() -> new AccountNotFoundException());
        return TransactionsRepository.getInstance().getByAccountId(accountId);
    }

    private void validateManager(int managerId) throws ManagerNotFoundException {
        if(!UsersRepository.getInstance().findById(managerId).orElseThrow(() -> new ManagerNotFoundException()).getRole().equals(Role.MANAGER)) {
            throw new ManagerNotFoundException();
        }
    }
}
