package studyplanner.texteditor;

import jakarta.persistence.*;
import studyplanner.user.User;

@Entity
@Table(name = "textEditors")
public class TextEditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
