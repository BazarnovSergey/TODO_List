package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;

import static ru.job4j.todo.util.CheckHttpSession.checkUserAuthorization;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String allTasks(Model model, HttpSession httpSession) {
        model.addAttribute("allTasks", taskService.findAll());
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "tasks";
    }

    @GetMapping("/formAddTask")
    public String formAddTask(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        model.addAttribute("user", user);
        return "addTask";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task) {
        taskService.add(task);
        return "redirect:/tasks";
    }

    @GetMapping("/task/{taskId}")
    public String task(Model model, HttpSession httpSession,
                       @PathVariable("taskId") int id) {
        model.addAttribute("task", taskService.findById(id));
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "task";
    }

    @GetMapping("/formUpdateTask/{taskId}")
    public String formUpdateTask(Model model, HttpSession httpSession,
                                 @PathVariable("taskId") int id) {
        model.addAttribute("task", taskService.findById(id));
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "updateTask";
    }

    @PostMapping("/updateTask")
    public String updatePost(@ModelAttribute Task task,
                             Model model, HttpSession httpSession) {
        taskService.update(task);
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "redirect:/tasks";
    }

    @GetMapping("/allCompletedTasks")
    public String allCompletedTasks(Model model, HttpSession httpSession) {
        model.addAttribute("allCompletedTasks", taskService.findCompletedTasks());
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "completedTasks";
    }

    @GetMapping("/allNotCompletedTasks")
    public String allNotCompletedTasks(Model model, HttpSession httpSession) {
        model.addAttribute("allNotCompletedTasks", taskService.findNotCompletedTasks());
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "notCompletedTasks";
    }

    @PostMapping("/formDelete")
    public String formDelete(Model model, HttpSession httpSession,
                             @ModelAttribute Task task) {
        taskService.delete(task);
        return "redirect:/tasks";
    }

    @PostMapping("/formCompleted")
    public String formCompleted(Model model, HttpSession httpSession,
                                @ModelAttribute Task task) {
        taskService.makeTaskComplete(task);
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "redirect:/tasks";
    }
}
