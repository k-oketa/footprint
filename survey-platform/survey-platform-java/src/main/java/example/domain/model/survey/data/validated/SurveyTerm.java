package example.domain.model.survey.data.validated;

import java.time.LocalDate;

public record SurveyTerm(
        LocalDate from,
        LocalDate to) {
}
