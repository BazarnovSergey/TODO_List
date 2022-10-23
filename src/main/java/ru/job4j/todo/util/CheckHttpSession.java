package ru.job4j.todo.util;

import org.springframework.ui.Model;
import ru.job4j.todo.model.User;

public final class CheckHttpSession {

    public static void checkUserAuthorization(Model model, User user) {
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }

}
