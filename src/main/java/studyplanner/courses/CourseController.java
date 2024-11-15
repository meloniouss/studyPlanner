package studyplanner.courses;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @PostMapping //Create, here we are using once again oauth2user id which we recently added
    public ResponseEntity<Course> createCourse(@RequestBody Course course, @AuthenticationPrincipal OAuth2User oauth2User) {
        System.out.println("creating course");
        String oauth2UserId = oauth2User.getAttribute("sub");
        User user = userService.findByOauth2Id(oauth2UserId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // User not found
        }
        course.setUser(user);
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping() //Read
    public ResponseEntity<List<Course>> getCoursesByUserId(@AuthenticationPrincipal OAuth2User oauth2User) {
    System.out.println("getting courses");
        String oauth2UserId = oauth2User.getAttribute("sub");
        List<Course> courses = courseService.getAllCoursesByOauth2UserId(oauth2UserId);
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
