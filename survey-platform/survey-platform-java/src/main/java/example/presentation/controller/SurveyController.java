package example.presentation.controller;

import example.application.UseCase;
import example.presentation.model.survey.SurveyPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("survey")
@RequiredArgsConstructor
public class SurveyController {

    private final UseCase useCase;

    @PostMapping("create")
    public void createSurvey(@RequestBody SurveyPayload surveyPayload) {
        var unvalidatedSurvey = surveyPayload.convert();
        this.useCase.createSurvey(unvalidatedSurvey);
    }
}
