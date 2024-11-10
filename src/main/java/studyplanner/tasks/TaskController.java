package studyplanner.tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studyplanner.courses.Course;
import studyplanner.courses.CourseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses/{courseId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CourseService courseService;

    @Autowired
    public TaskController(TaskService taskService, CourseService courseService) {
        this.taskService = taskService;
        this.courseService = courseService;
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
    public ResponseEntity<Task> createTask(@PathVariable Long courseId, @RequestBody Task task) {
        try {
            Optional<Course> optionalCourse = courseService.getCourseById(courseId);

            if (optionalCourse.isPresent()) {
                task.setCourse(optionalCourse.get());  // Extract Course from Optional and set
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Task createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping // TO-DO FIX THIS
    public ResponseEntity<List<Task>> updateTasks(@PathVariable Long courseId, @RequestBody List<Task> updatedTasks) {
        List<Task> existingTasks = taskService.getAllTasksForCourse(courseId);
        if (existingTasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        updatedTasks.forEach(updatedTask -> {
            existingTasks.stream()
                    .filter(task -> task.getId().equals(updatedTask.getId()))
                    .findFirst()
                    .ifPresent(existingTask -> {
                        existingTask.setTaskName(updatedTask.getTaskName());
                        existingTask.setTaskDescription(updatedTask.getTaskDescription());
                    });
        });

        taskService.updateTasks(existingTasks);

        return ResponseEntity.ok(existingTasks);
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
