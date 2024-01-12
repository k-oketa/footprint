package example.application.service;

import example.domain.model.survey.SurveyRepository;
import example.domain.model.survey.data.unvalidated.UnvalidatedSurvey;
import example.domain.model.survey.function.ValidateSurvey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyCreatingService {

    private final SurveyRepository surveyRepository;

    public void createSurvey(UnvalidatedSurvey unvalidatedSurvey) {
        var validatedSurvey = new ValidateSurvey().apply(unvalidatedSurvey)
                .getOrElseThrow(message -> new IllegalArgumentException(message));
        this.surveyRepository.createSurvey(validatedSurvey);
    }
}
