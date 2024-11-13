package studyplanner.tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import studyplanner.courses.Course;
import studyplanner.courses.CourseService;

import java.util.ArrayList;
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
    public ResponseEntity<List<Task>> getAllTasksForCourse(@PathVariable Long courseId, @AuthenticationPrincipal OAuth2User oauth2User) {
        String oauth2UserId = oauth2User.getAttribute("sub");
        List<Course> courses = courseService.getAllCoursesByOauth2UserId(oauth2UserId);
        List<Task> tasks = taskService.getAllTasksForCourse(courseId);
        for (Course course : courses) {
            if (course.getId().equals(courseId) && course.getUser().getOauth2UserId().equals(oauth2UserId)) {
                System.out.println("User with OAuth2 ID: " + oauth2UserId + " accessed course with ID: " + course.getId()); // DO NOT use string template
                System.out.println("Course id is" + courseId);
                return ResponseEntity.ok(tasks);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
                task.setCourse(optionalCourse.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Task createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
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
                        existingTask.setIsDone(updatedTask.getIsDone());
                        existingTask.setTaskDescription(updatedTask.getTaskDescription());
                        existingTask.setDueDate((updatedTask.getDueDate()));
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
