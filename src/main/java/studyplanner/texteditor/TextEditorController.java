package studyplanner.texteditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import studyplanner.courses.Course;
import studyplanner.user.User;
import studyplanner.user.UserService;

import java.util.Optional;

@RestController
public class TextEditorController {
    private final TextEditorService service;
    private final UserService userService;
    @Autowired
    public TextEditorController(TextEditorService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    public TextEditor createOrUpdateTextEditor(@RequestBody TextEditor textEditor,@AuthenticationPrincipal OAuth2User oauth2User ) {
        User user = userService.findByOauth2Id(oauth2User.getAttribute("sub"));
        Optional<TextEditor> existingTextEditor = service.getTextEditorByOauthId(user.getOauth2UserId());
        if (existingTextEditor.isPresent()) {
            textEditor.setId(existingTextEditor.get().getId());
            textEditor.setUser(user);
        } else {
            textEditor.setUser(user);
        }
        return service.createOrUpdateTextEditor(textEditor);
    }

    // Get a TextEditor by ID
    @GetMapping()
    public ResponseEntity<Object> getTextEditorByOAuthId(@AuthenticationPrincipal OAuth2User oauth2User) {
        String oauth2UserId = oauth2User.getAttribute("sub");
        User user = userService.findByOauth2Id(oauth2UserId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // User not found
        }

        Optional<TextEditor> textEditor = service.getTextEditorByOauthId(oauth2UserId);
        if(textEditor.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(textEditor);
    }

}
