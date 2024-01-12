package example.presentation.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SurveyControllerTests {

    private final MockMvc mvc;
    private final JdbcClient jdbc;
    private final String url = "http://localhost:8080/survey/create";

    @Autowired
    public SurveyControllerTests(MockMvc mvc, JdbcClient jdbc) {
        this.mvc = mvc;
        this.jdbc = jdbc;
    }

    @Test
    void createSurvey() throws Exception {
        var json = """
                {
                    "surveyName": "高校生・高専生の数学に対する意識調査",
                    "from": "2024-01-01",
                    "to": "2024-01-31",
                    "questions": [
                        {
                            "questionType": "singleChoiceQuestion",
                            "questionSentence": "あなたは高校生ですか？高専生ですか？",
                            "options": ["高校生", "高専生"]
                        }
                    ]
                }
                """;
        this.mvc.perform(MockMvcRequestBuilders.post(this.url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
