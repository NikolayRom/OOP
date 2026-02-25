import console.ActionRouter;
import console.AuthMenu;
import model.*;
import repository.BanksRepository;
import repository.CompaniesRepository;
import repository.UsersRepository;

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

        BanksRepository.getInstance().save(new Bank("СберБанк"));
        BanksRepository.getInstance().save(new Bank("Тинькофф"));
        BanksRepository.getInstance().save(new Bank("Альфа-Банк"));

        CompaniesRepository.getInstance().save(new Company("Google", CompanyType.LLC));
        CompaniesRepository.getInstance().save(new Company("Газпром", CompanyType.OJSC));
        CompaniesRepository.getInstance().save(new Company("Шаурма у Ашота", CompanyType.IE));

        Admin admin = new Admin("admin", "admin", "Главный Администратор");
        UsersRepository.getInstance().save(admin);

        Manager manager = new Manager("manager", "manager", "Иван Менеджер");
        UsersRepository.getInstance().save(manager);

        Client client = new Client("client", "client", "Петр Клиентов");
        client.setStatus(ClientStatus.ACTIVE); 
        UsersRepository.getInstance().save(client);
    
        int firstCompanyId = CompaniesRepository.getInstance().getAll().getFirst().getId();
        CompaniesRepository.getInstance().findById(firstCompanyId).ifPresent(c -> c.addEmployee(client.getId()));
        client.setCompanyId(firstCompanyId);
    }

    private static void printCredentials() {
        System.out.println("\nТестовые учетные записи:");
        System.out.println(" - Админ:    admin / admin");
        System.out.println(" - Менеджер: manager / manager");
        System.out.println(" - Клиент:   client / client");
        System.out.println("-----------------------------------------");
    }
}