package studyplanner.courses;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    //Create,read,update,delete
    public Course createCourse(Course newCourse){
        return courseRepository.save(newCourse);
    }



    List<Course> getAllCoursesByUserId(Long id){
        return courseRepository.findByUserId(id);
    }

    public Optional<Course> getCourseById(Long id){
        return courseRepository.findById(id);
    }
    public Course updateCourse(Long id, Course modifiedCourse){ //might not need the ID argument
        return courseRepository.findById(id).map(course -> {
            course.setCourseName(modifiedCourse.getCourseName());
            return courseRepository.save(course);
        }).orElseThrow(() -> new RuntimeException("Course not found"));
    }
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

}
