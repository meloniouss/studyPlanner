package studyplanner.courses;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studyplanner.user.User;
import studyplanner.user.UserService;


import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private final CourseService courseService;
    private final UserService userService;


    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping //Create
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        System.out.println("creating course");
        Long userId = course.getUser().getId(); // Assuming the course object has user with id
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // User not found
        }
        course.setUser(user);
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping() //Read
    public ResponseEntity<List<Course>> getCoursesByUserId(@CookieValue("userId") Long userId) {
        List<Course> courses = courseService.getAllCoursesByUserId(userId);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build(); // No content found, it sends an OK request but that is it
        }
        return ResponseEntity.ok(courses); //returns the list
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        Course course = courseService.updateCourse(id, updatedCourse);
        return ResponseEntity.ok(course); // Return the updated course with a 200 OK status
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build(); // Return a 204 No Content status after deletion (doesnt display anything)
    }

}
