package example.infrastructure.datasource.survey.model;

import example.domain.model.survey.data.validated.Survey;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SurveyDTO {

    private String surveyName;
    private LocalDate from;
    private LocalDate to;

    public SurveyDTO(Survey survey) {
        this.surveyName = survey.surveyName().value();
        this.from = survey.surveyTerm().from();
        this.to = survey.surveyTerm().to();
    }
}
