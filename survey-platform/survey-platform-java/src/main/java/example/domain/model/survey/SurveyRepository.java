package example.domain.model.survey;

import example.domain.model.survey.data.validated.Survey;

public interface SurveyRepository {
    void createSurvey(Survey survey);
}
