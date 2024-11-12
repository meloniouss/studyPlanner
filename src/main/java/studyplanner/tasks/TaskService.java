package studyplanner.tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Task> tasksToSave = tasks.stream()
                .filter(task -> !task.getIsDone())
                .collect(Collectors.toList());

        List<Task> tasksToDelete = tasks.stream()
                .filter(Task::getIsDone)
                .collect(Collectors.toList());

        taskRepository.deleteAll(tasksToDelete);
        return taskRepository.saveAll(tasksToSave);
    }
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
