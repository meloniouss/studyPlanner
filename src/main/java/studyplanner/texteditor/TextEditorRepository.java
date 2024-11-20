package studyplanner.texteditor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.Text;
import studyplanner.courses.Course;
import studyplanner.tasks.Task;


import java.util.List;
import java.util.Optional;

public interface TextEditorRepository extends JpaRepository<TextEditor, Long> {
    Optional<TextEditor> findByUser_Oauth2UserId(String oauth2UserId);
}
