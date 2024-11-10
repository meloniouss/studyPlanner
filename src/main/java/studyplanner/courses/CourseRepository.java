package studyplanner.courses;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByUserId(Long userId);
    List<Course> findByUser_Oauth2UserId(String oauth2UserId);
}

