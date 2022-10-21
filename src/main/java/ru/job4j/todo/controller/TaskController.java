package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String allTasks(Model model) {
        model.addAttribute("allTasks", taskService.findAll());
        return "tasks";
    }

    @GetMapping("/formAddTask")
    public String formAddTask() {
        return "addTask";
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task) {
        taskService.add(task);
        return "redirect:/tasks";
    }

    @GetMapping("/task/{taskId}")
    public String task(Model model,
                       @PathVariable("taskId") int id) {
        model.addAttribute("task", taskService.findById(id));
        return "task";
    }

    @GetMapping("/formUpdateTask/{taskId}")
    public String formUpdateTask(Model model,
                                 @PathVariable("taskId") int id) {
        model.addAttribute("task", taskService.findById(id));
        return "updateTask";
    }

    @PostMapping("/updateTask")
    public String updatePost(@ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/tasks";
    }

    @GetMapping("/allCompletedTasks")
    public String allCompletedTasks(Model model) {
        model.addAttribute("allCompletedTasks", taskService.findCompletedTasks());
        return "completedTasks";
    }

    @GetMapping("/allNotCompletedTasks")
    public String allNotCompletedTasks(Model model) {
        model.addAttribute("allNotCompletedTasks", taskService.findNotCompletedTasks());
        return "notCompletedTasks";
    }

    @PostMapping("/formDelete")
    public String formDelete(@ModelAttribute Task task) {
        taskService.delete(task);
        return "redirect:/tasks";
    }

    @PostMapping("/formCompleted")
    public String formCompleted(@ModelAttribute Task task) {
        taskService.makeTaskComplete(task);
        return "redirect:/tasks";
    }
}
