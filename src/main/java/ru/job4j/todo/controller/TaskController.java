package ru.job4j.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.job4j.todo.util.CheckHttpSession.checkUserAuthorization;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    public TaskController(TaskService taskService, PriorityService priorityService, CategoryService categoryService) {
        this.taskService = taskService;
        this.priorityService = priorityService;
        this.categoryService = categoryService;
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
        model.addAttribute("categories", categoryService.findAll());
        return "addTask";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task, @RequestParam("priority.id") Integer priorityId,
                             @RequestParam("categoriesIDList") List<Integer> categoriesIDList,
                             Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        model.addAttribute("user", user);
        Optional<Priority> optionalPriority = priorityService.findById(priorityId);
        if (optionalPriority.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Category> categoryList = categoriesIDList.stream()
                .map(id -> categoryService.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .toList();
        task.setCategories(categoryList);
        task.setPriority(optionalPriority.get());
        task.setUser(user);
        task.setCreated(user.getUserZone());
        taskService.add(task);
        return "redirect:/tasks";
    }

    @GetMapping("/task/{taskId}")
    public String task(Model model, HttpSession httpSession,
                       @PathVariable("taskId") int id) {
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("task", optionalTask.get());
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "task";
    }

    @GetMapping("/formUpdateTask/{taskId}")
    public String formUpdateTask(Model model, HttpSession httpSession,
                                 @PathVariable("taskId") int id) {
        model.addAttribute("priorities", priorityService.findAll());
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("task", optionalTask.get());
        User user = (User) httpSession.getAttribute("user");
        checkUserAuthorization(model, user);
        return "updateTask";
    }

    @PostMapping("/updateTask")
    public String updatePost(@ModelAttribute Task task, @RequestParam("priority.id") Integer priorityId,
                             Model model, HttpSession httpSession) {
        Optional<Priority> optionalPriority = priorityService.findById(priorityId);
        if (optionalPriority.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        task.setCreated(LocalDateTime.now());
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

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "404";
    }
}
