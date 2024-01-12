package example.presentation.model.survey;

import example.domain.model.survey.data.unvalidated.UnvalidatedSurvey;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SurveyPayload {
    private String surveyName;
    private LocalDate from;
    private LocalDate to;
    private List<QuestionPayload> questions;

    public UnvalidatedSurvey convert() {
        return new UnvalidatedSurvey(
                this.surveyName,
                this.from,
                this.to,
                this.questions.stream().map(QuestionPayload::convert).toList()
        );
    }
}
