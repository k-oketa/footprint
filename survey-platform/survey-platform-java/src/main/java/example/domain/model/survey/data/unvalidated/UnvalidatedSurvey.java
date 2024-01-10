package example.domain.model.survey.data.unvalidated;

import java.time.LocalDate;
import java.util.List;

public record UnvalidatedSurvey(
        String surveyName,
        LocalDate from,
        LocalDate to,
        List<UnvalidatedQuestion> questions) {
}
