package example.presentation.controller;

import example.presentation.model.survey.SurveyPayload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("survey")
public class SurveyController {

    @PostMapping("create")
    public void createSurvey(SurveyPayload surveyPayload) {

    }
}
