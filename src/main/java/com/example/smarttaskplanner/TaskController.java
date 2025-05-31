package com.example.smarttaskplanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/data")
public class TaskController{

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) String sort) {
        if (sort != null) {
            if (sort.startsWith("-")) {
                String field = sort.substring(1);
                return taskRepository.findAll(Sort.by(Sort.Direction.DESC, field));
            }else {
                return taskRepository.findAll(Sort.by(Sort.Direction.ASC, sort));
            }
        }else {
            return taskRepository.findAll();
        }
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedtask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedtask.getTitle());
            task.setDescription(updatedtask.getDescription());
            task.setDifficulty(updatedtask.getDifficulty());
            task.setEstimatedTime(updatedtask.getEstimatedTime());
            task.setDeadline(updatedtask.getDeadline());
            task.setCompleted(updatedtask.isCompleted());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
