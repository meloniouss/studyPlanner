package studyplanner.studyplan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyPlanService {
    private final StudyPlanRepository studyPlanRepository;

    @Autowired
    public StudyPlanService(StudyPlanRepository studyPlanRepository) {
        this.studyPlanRepository = studyPlanRepository;
    }

    public List<StudyPlan> getAllStudyPlans() {
        return studyPlanRepository.findAll();
    }

    public StudyPlan createStudyPlan(StudyPlan studyPlan) {
        return studyPlanRepository.save(studyPlan);
    }

    public Optional<StudyPlan> getStudyPlanById(Long id) {
        return studyPlanRepository.findById(id);
    }

    public StudyPlan updateStudyPlan(Long id, StudyPlan updatedStudyPlan) {
        return studyPlanRepository.findById(id).map(studyPlan -> {
            studyPlan.setTitle(updatedStudyPlan.getTitle());
            studyPlan.setStartDate(updatedStudyPlan.getStartDate());
            studyPlan.setEndDate(updatedStudyPlan.getEndDate());
            studyPlan.setDescription(updatedStudyPlan.getDescription());
            return studyPlanRepository.save(studyPlan);
        }).orElseThrow(() -> new RuntimeException("StudyPlan not found"));
    }

    public void deleteStudyPlan(Long id) {
        studyPlanRepository.deleteById(id);
    }
}
