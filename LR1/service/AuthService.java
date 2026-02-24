package service;

import exception.*;
import model.Client;
import model.ClientStatus;
import model.Request;
import model.RequestType;
import model.User;
import repository.RequestsRepository;
import repository.UsersRepository;

public class AuthService {
    private static AuthService instance;
    private AuthService() {}
    public AuthService getInstance() {
        if(instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public User login(String login, String password) throws InvalidLoginException, AccessDeniedException, RequestPendingException {
        User user = UsersRepository.getInstance().getAll().stream().filter(usr -> usr.getLogin().equals(login)).findFirst().orElseThrow(() -> new InvalidLoginException());
        if(!user.getPassword().equals(password)) {
            throw new InvalidLoginException();
        }

        if(user instanceof Client) {
            if(((Client)user).getStatus().equals(ClientStatus.PENDING)) {
                throw new RequestPendingException();
            }
            if(((Client)user).getStatus().equals(ClientStatus.BLOCKED)) {
                throw new AccessDeniedException();
            }
        }

        return user;
    }

    public String registerClient(String login, String password, String name) throws LoginExistingException {
        if(UsersRepository.getInstance().getAll().stream().anyMatch(usr -> usr.getLogin().equals(login))) {
            throw new LoginExistingException();
        }
        Client client = new Client(login, password, name);
        UsersRepository.getInstance().save(client);
        RequestsRepository.getInstance().push(new Request(RequestType.REGISTRATION, client.getId()));
        return "Заявка на регистрацию отправлена. ID клиента: " + client.getId();
    }
}
