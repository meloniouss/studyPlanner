package studyplanner.texteditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TextEditorService {
    private final TextEditorRepository repository;

    @Autowired
    public TextEditorService(TextEditorRepository repository) {
        this.repository = repository;
    }

    public TextEditor createOrUpdateTextEditor(TextEditor textEditor) {
        return repository.save(textEditor);
    }

    public Optional<TextEditor> getTextEditorByOauthId(String id) {
        return repository.findByUser_Oauth2UserId(id);
    }

}
