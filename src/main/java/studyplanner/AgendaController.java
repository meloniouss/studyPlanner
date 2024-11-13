package studyplanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import studyplanner.courses.Course;
import studyplanner.courses.CourseService;
import studyplanner.tasks.Task;
import studyplanner.tasks.TaskService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AgendaController {

    private final TaskService taskService;
    private final CourseService courseService;

    public AgendaController(TaskService taskService, CourseService courseService) {
        this.taskService = taskService;
        this.courseService = courseService;
    }


    @GetMapping("/calendar")
    public ResponseEntity<List<Task>> getAllTasksForAllCoursesPerUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


        String oauth2UserId = oauth2User.getAttribute("sub");

        if (oauth2UserId == null || oauth2UserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


        List<Course> userCourses = courseService.getAllCoursesByOauth2UserId(oauth2UserId);
        List<Task> allTasks = new ArrayList<>();
        for (Course course : userCourses) {
            List<Task> courseTasks = taskService.getAllTasksForCourse(course.getId());
            allTasks.addAll(courseTasks);
        }

        if (allTasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        System.out.println("User with OAuth2 ID: " + oauth2UserId + " accessed all tasks for their courses.");
        return ResponseEntity.ok(allTasks);
    }
}
