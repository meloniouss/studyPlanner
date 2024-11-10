package studyplanner.tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasksForCourse(Long courseId) {
        return taskRepository.findByCourseId(courseId);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTaskName(taskDetails.getTaskName());
        task.setIsDone(taskDetails.getIsDone());
        task.setDueDate(taskDetails.getDueDate());
        task.setTaskDescription(taskDetails.getTaskDescription());
        return taskRepository.save(task);
    }
    public List<Task> updateTasks(List<Task> tasks) {
        return taskRepository.saveAll(tasks); // Batch save for updates
    }
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
