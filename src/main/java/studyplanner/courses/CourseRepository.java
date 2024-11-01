package studyplanner.courses;

import org.springframework.data.jpa.repository.JpaRepository;
import studyplanner.studyplan.StudyPlan;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByUserId(Long userId);

}

