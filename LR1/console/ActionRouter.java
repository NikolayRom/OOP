package console;

import model.User;
import model.Role;

public class ActionRouter {
    public void route(User user) {
        if(user == null) return;

        System.out.println("\n=====Добро пожаловать, " + user.getName() + " [" + user.getRole() + "] =====\n");

        switch(user.getRole()) {
            case Role.CLIENT:
                new ClientMenu(user).show();
                break;
            case Role.MANAGER:
                new ManagerMenu(user).show();
                break;
            case Role.ADMIN:
                new AdminMenu().show();
                break;
        }
    }
}
