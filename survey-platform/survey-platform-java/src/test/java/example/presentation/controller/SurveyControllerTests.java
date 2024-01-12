package example.presentation.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
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
                            "questionSentence": "あなたは高校生ですか？高専生ですか？",
                            "questionType": "singleChoice",
                            "options": ["高校生", "高専生"]
                        },
                        {
                            "questionSentence": "履修した数学の科目を答えてください。",
                            "questionType": "multipleChoice",
                            "options": ["数学1", "数学A", "数学2", "数学B", "数学3", "数学C"]
                        }
                    ]
                }
                """;
        this.mvc.perform(MockMvcRequestBuilders.post(this.url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());

        var survey = this.jdbc.sql("""
                select * from survey
                """)
                        .query()
                        .singleRow();
        Assertions.assertEquals("高校生・高専生の数学に対する意識調査", survey.get("survey_name"));
        Assertions.assertEquals(Date.valueOf("2024-01-01"), survey.get("term_from"));
        Assertions.assertEquals(Date.valueOf("2024-01-31"), survey.get("term_to"));

        var singleChoiceQuestion = this.jdbc.sql("""
                select * from single_choice_question
                """)
                        .query()
                        .singleRow();
        Assertions.assertEquals(1L, singleChoiceQuestion.get("survey_id"));
        Assertions.assertEquals("あなたは高校生ですか？高専生ですか？", singleChoiceQuestion.get("question_sentence"));
        Assertions.assertEquals(0, singleChoiceQuestion.get("question_ordinal"));

        var multipleChoiceQuestion = this.jdbc.sql("""
                select * from multiple_choice_question
                """)
                .query()
                .singleRow();
        Assertions.assertEquals(1L, multipleChoiceQuestion.get("survey_id"));
        Assertions.assertEquals("履修した数学の科目を答えてください。", multipleChoiceQuestion.get("question_sentence"));
        Assertions.assertEquals(1, multipleChoiceQuestion.get("question_ordinal"));

        var options = this.jdbc.sql("""
                select * from option
                """)
                .query()
                .listOfRows();
        Assertions.assertEquals(1L, options.get(0).get("survey_id"));
        Assertions.assertEquals(0, options.get(0).get("question_ordinal"));
        Assertions.assertEquals(0, options.get(0).get("option_ordinal"));
        Assertions.assertEquals("高校生", options.get(0).get("option_sentence"));
        Assertions.assertEquals(1L, options.get(1).get("survey_id"));
        Assertions.assertEquals(0, options.get(1).get("question_ordinal"));
        Assertions.assertEquals(1, options.get(1).get("option_ordinal"));
        Assertions.assertEquals("高専生", options.get(1).get("option_sentence"));
        Assertions.assertEquals(1L, options.get(2).get("survey_id"));
        Assertions.assertEquals(1, options.get(2).get("question_ordinal"));
        Assertions.assertEquals(0, options.get(2).get("option_ordinal"));
        Assertions.assertEquals("数学1", options.get(2).get("option_sentence"));
    }
}
