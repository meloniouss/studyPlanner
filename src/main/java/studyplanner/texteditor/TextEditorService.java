package studyplanner.texteditor;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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
