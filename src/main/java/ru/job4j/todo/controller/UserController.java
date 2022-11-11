package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user,
                               @RequestParam(name = "zoneId") String zone
    ) {
        Optional<User> regUser = userService.findByLogin(user.getLogin());
        if (regUser.isPresent()) {
            model.addAttribute(
                    "message", "Пользователь с такой почтой уже существует");
            return "redirect:/fail";
        }
        user.setUserZone(LocalDateTime.now(ZoneId.of(zone)));
        userService.add(user);
        return "redirect:/success";
    }

    @GetMapping("/newUser")
    public String formRegistration(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("timeZones", userService.getAvailableZones());
        return "addNewUser";
    }

    @GetMapping("/success")
    public String successfulRegistration() {
        return "successfulRegistration";
    }

    @GetMapping("/fail")
    public String failRegistration() {
        return "failRegistration";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findByLoginAndPassword(
                user.getLogin(), user.getPassword());
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}