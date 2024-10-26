package studyplanner.studyplan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/study-plans")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @GetMapping
    public List<StudyPlan> getAllStudyPlans() {
        return studyPlanService.getAllStudyPlans();
    }

    @PostMapping
    public StudyPlan createStudyPlan(@RequestBody StudyPlan studyPlan) {
        return studyPlanService.createStudyPlan(studyPlan);
    }

    @GetMapping("/{id}")
    public StudyPlan getStudyPlanById(@PathVariable Long id) {
        return studyPlanService.getStudyPlanById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study plan not found"));
    }

    @PutMapping("/{id}")
    public StudyPlan updateStudyPlan(@PathVariable Long id, @RequestBody StudyPlan updatedStudyPlan) {
        return studyPlanService.updateStudyPlan(id, updatedStudyPlan);
    }

    @DeleteMapping("/{id}")
    public void deleteStudyPlan(@PathVariable Long id) {
        studyPlanService.deleteStudyPlan(id);
    }
}
