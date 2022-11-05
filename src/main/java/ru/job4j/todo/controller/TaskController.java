package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Optional;

import static ru.job4j.todo.util.CheckHttpSession.checkUserAuthorization;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final PriorityService priorityService;

    public TaskController(TaskService taskService, PriorityService priorityService) {
        this.taskService = taskService;
        this.priorityService = priorityService;
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
        model.addAttribute("priorities", priorityService.findAll());
        return "addTask";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task, @RequestParam("priority.id") Integer priorityId,
                             Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        model.addAttribute("user", user);
        Optional<Priority> optionalPriority = priorityService.findById(priorityId);
        optionalPriority.ifPresent(task::setPriority);
        task.setUser(user);
        taskService.add(task);
        return "redirect:/tasks";
    }

    @GetMapping("/task/{taskId}")
    public String task(Model model, HttpSession httpSession,
                       @PathVariable("taskId") int id) {
        Optional<Task> optionalTask = taskService.findById(id);
        optionalTask.ifPresent(task -> model.addAttribute("task", task));
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "task";
    }

    @GetMapping("/formUpdateTask/{taskId}")
    public String formUpdateTask(Model model, HttpSession httpSession,
                                 @PathVariable("taskId") int id) {
        model.addAttribute("priorities", priorityService.findAll());
        Optional<Task> optionalTask = taskService.findById(id);
        optionalTask.ifPresent(task -> model.addAttribute("task", task));
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "updateTask";
    }

    @PostMapping("/updateTask")
    public String updatePost(@ModelAttribute Task task, @RequestParam("priority.id") Integer priorityId,
                             Model model, HttpSession httpSession) {
        Optional<Priority> optionalPriority = priorityService.findById(priorityId);
        optionalPriority.ifPresent(task::setPriority);
        task.setCreated(LocalDate.now());
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        task.setUser(user);
        taskService.update(task);
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
    public String formDelete(@ModelAttribute Task task) {
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