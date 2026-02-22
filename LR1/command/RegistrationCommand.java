package command;

import model.ClientStatus;
import repository.UsersRepository;
import exception.ClientNotFoundException;
import model.Client;

public class RegistrationCommand extends AbstractCommand {
    private int targetClientId;
    private ClientStatus oldStatus;

    public RegistrationCommand(int userId, int targetClientId) {
        super(userId);
        this.targetClientId = targetClientId;
    }

    public int getTargetClientId() {
        return this.targetClientId;
    }
    public ClientStatus getOldStatus() {
        return this.oldStatus;
    }
    public void setOldStatus(ClientStatus status) {
        this.oldStatus = status;
    }

    @Override
    public String execute() throws ClientNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getTargetClientId()).filter(user -> user instanceof Client).orElseThrow(() -> new ClientNotFoundException());
        setOldStatus(client.getStatus());
        client.setStatus(ClientStatus.ACTIVE);
        return "Выполнено: Клиент " + getTargetClientId() + " успешно зарегистрирован (активирован).";
    }
    @Override
    public String undo() throws ClientNotFoundException {
        Client client = (Client) UsersRepository.getInstance().findById(getTargetClientId()).filter(user -> user instanceof Client).orElseThrow(() -> new ClientNotFoundException());
        client.setStatus(getOldStatus());
        return "Отмена: Регистрация клиента " + getTargetClientId() + " отменена. Статус вернулся к " + getOldStatus();
    }
    @Override
    public String toString() {
        return "Command: Подтверждение регистрации. ClientID: " + getTargetClientId();
    }
}
