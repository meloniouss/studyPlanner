package studyplanner.courses;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping //Create
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping("/courses") //Read
    public ResponseEntity<List<Course>> getCoursesByUserId(@CookieValue("userId") Long userId) {
        List<Course> courses = courseService.getAllCoursesByUserId(userId);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build(); // No content found, it sends an OK request but thats it
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
