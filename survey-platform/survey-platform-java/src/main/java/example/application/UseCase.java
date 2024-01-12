package example.application;

import example.application.service.SurveyCreatingService;
import example.domain.model.survey.data.unvalidated.UnvalidatedSurvey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UseCase {

    private final SurveyCreatingService surveyCreatingService;

    public void createSurvey(UnvalidatedSurvey unvalidatedSurvey) {
        this.surveyCreatingService.createSurvey(unvalidatedSurvey);
    }
}
