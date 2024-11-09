package studyplanner.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import studyplanner.tasks.Task;


import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCourseId(Long userId);

}

