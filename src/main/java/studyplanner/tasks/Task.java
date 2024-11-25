package studyplanner.tasks;
import jakarta.persistence.*;
import studyplanner.courses.Course;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_name", nullable = true)
    private String taskName;

    @Column(name = "is_done", nullable = true)
    private Boolean isDone;

    @Column(name = "due_date", nullable = true)
    private LocalDate dueDate;

    @Column(name = "task_description")
    private String taskDescription;


    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;

    // Constructors
    public Task() {}

    public Task(String taskName, Boolean isDone, LocalDate dueDate, String taskDescription, Course course) {
        this.taskName = taskName;
        this.isDone = isDone;
        this.dueDate = dueDate;
        this.taskDescription = taskDescription;
        this.course = course;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public Boolean getIsDone() { return isDone; }
    public void setIsDone(Boolean isDone) { this.isDone = isDone; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getTaskDescription() { return taskDescription; }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
