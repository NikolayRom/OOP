package command;

import model.ClientStatus;
import repository.UsersRepository;
import exception.ClientNotFoundException;
import model.Client;

public class RegistrationCommand extends AbstractCommand {
    private ClientStatus oldStatus;

    public RegistrationCommand(int userId) {
        super(userId);
    }

    public ClientStatus getOldStatus() {
        return this.oldStatus;
    }
    public void setOldStatus(ClientStatus status) {
        this.oldStatus = status;
    }

    @Override
    public String execute() throws ClientNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).filter(user -> user instanceof Client).orElseThrow(() -> new ClientNotFoundException());
        setOldStatus(client.getStatus());
        client.setStatus(ClientStatus.ACTIVE);
        return "Выполнено: Клиент " + getUserId() + " успешно зарегистрирован (активирован).";
    }
    @Override
    public String undo() throws ClientNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getUserId()).filter(user -> user instanceof Client).orElseThrow(() -> new ClientNotFoundException());
        client.setStatus(getOldStatus());
        return "Отмена: Регистрация клиента " + getUserId() + " отменена. Статус вернулся к " + getOldStatus();
    }
    @Override
    public String toString() {
        return "Command: Подтверждение регистрации. ClientID: " + getUserId();
    }
}
