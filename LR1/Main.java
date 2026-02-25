import java.math.BigDecimal;

import console.ActionRouter;
import console.AuthMenu;
import model.*;
import repository.BanksRepository;
import repository.CompaniesRepository;
import repository.UsersRepository;
import repository.AccountsRepository;

public class Main {

    public static void main(String[] args) {
        initData();

        AuthMenu authMenu = new AuthMenu();
        ActionRouter actionRouter = new ActionRouter();

        System.out.println("=========================================");
        System.out.println("         СИСТЕМА УПРАВЛЕНИЯ ФИНАНСАМИ    ");
        System.out.println("=========================================");
        printCredentials();

        while (true) {
            try {
                User activeUser = authMenu.show();

                if (activeUser != null) {
                    actionRouter.route(activeUser);
                }
            } catch (Exception ex) {
                System.out.println("<<<MAIN ERROR: " + ex.getMessage() + ">>>");
            }
        }
    }

    private static void initData() {
        try {
            System.out.println("Инициализация данных...");
            
            BanksRepository banksRepo = BanksRepository.getInstance();
            CompaniesRepository companiesRepo = CompaniesRepository.getInstance();
            UsersRepository usersRepo = UsersRepository.getInstance();
            AccountsRepository accountsRepo = AccountsRepository.getInstance();

            Bank sber = new Bank("СберБанк");
            Bank tinkoff = new Bank("Тинькофф");
            Bank alpha = new Bank("Альфа-Банк");
            
            banksRepo.save(sber);
            banksRepo.save(tinkoff);
            banksRepo.save(alpha);

            Company google = new Company("Google Tech", CompanyType.LLC);
            Company gazprom = new Company("Газпром", CompanyType.OJSC);
            Company shawarma = new Company("ИП Шаурма у Ашота", CompanyType.IE);
            
            companiesRepo.save(google);
            companiesRepo.save(gazprom);
            companiesRepo.save(shawarma);

            Admin admin = new Admin("admin", "admin", "Сергей Админ");
            Manager manager = new Manager("manager", "manager", "Иван Менеджер");
            usersRepo.save(admin);
            usersRepo.save(manager);

            Client bob = new Client("bob", "bob", "Боб Рокфеллер");
            bob.setStatus(ClientStatus.ACTIVE);
            usersRepo.save(bob);

            google.addEmployee(bob.getId());
            bob.setCompanyId(google.getId());
            bob.setApplySalaryProject(new BigDecimal("350000"));

            DebitAccount bobDebit = new DebitAccount(bob.getId(), sber.getId());
            bobDebit.deposit(new BigDecimal("500000")); 
            accountsRepo.save(bobDebit);

            DepositAccount bobDeposit = new DepositAccount(bob.getId(), tinkoff.getId(), new BigDecimal("0.12"), 12);
            bobDeposit.deposit(new BigDecimal("1000000"));
            accountsRepo.save(bobDeposit);

            Client alice = new Client("alice", "alice", "Алиса Селезнева");
            alice.setStatus(ClientStatus.ACTIVE);
            usersRepo.save(alice);

            gazprom.addEmployee(alice.getId());
            alice.setCompanyId(gazprom.getId());
            alice.setApplySalaryProject(new BigDecimal("85000"));

            DebitAccount aliceDebit = new DebitAccount(alice.getId(), sber.getId());
            aliceDebit.deposit(new BigDecimal("45000"));
            accountsRepo.save(aliceDebit);

            Client eve = new Client("eve", "eve", "Ева Браун");
            eve.setStatus(ClientStatus.ACTIVE);
            usersRepo.save(eve);

            shawarma.addEmployee(eve.getId());
            eve.setCompanyId(shawarma.getId());
            eve.setApplySalaryProject(new BigDecimal("25000"));

            DebitAccount eveDebit = new DebitAccount(eve.getId(), alpha.getId());
            eveDebit.deposit(new BigDecimal("3000"));
            accountsRepo.save(eveDebit);

        } catch (Exception e) {
            System.err.println("<<<MAIN ERROR: " + e.getMessage() + ">>>");
        }
    }

    private static void printCredentials() {
        System.out.println("\nУчетные записи для входа:");
        System.out.println("1. Admin:   admin / admin");
        System.out.println("2. Manager: manager / manager");
        System.out.println("3. Client:  bob / bob");
        System.out.println("4. Client:  alice / alice");
        System.out.println("5. Client:  eve / eve");
        System.out.println("-----------------------------------------");
    }
}