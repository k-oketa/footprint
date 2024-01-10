package example.domain.model.survey.data.validated;

import java.util.List;

public record Survey(
        SurveyName surveyName,
        SurveyTerm surveyTerm,
        List<Question> questions) {
}
