package studyplanner.tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses/{courseId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasksForCourse(@PathVariable Long courseId) {
        return taskService.getAllTasksForCourse(courseId);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long courseId, @PathVariable Long taskId) {
        return taskService.getTaskById(taskId)
                .filter(task -> task.getCourse().getId().equals(courseId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Task createTask(@PathVariable Long courseId, @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long courseId, @PathVariable Long taskId, @RequestBody Task taskDetails) {
        return taskService.getTaskById(taskId)
                .filter(task -> task.getCourse().getId().equals(courseId))
                .map(task -> {
                    Task updatedTask = taskService.updateTask(taskId, taskDetails);
                    return ResponseEntity.ok(updatedTask);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long courseId, @PathVariable Long taskId) {
        Optional<Task> optionalTask = taskService.getTaskById(taskId);

        if (optionalTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = optionalTask.get();

        if (task.getCourse() == null || !task.getCourse().getId().equals(courseId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

}
