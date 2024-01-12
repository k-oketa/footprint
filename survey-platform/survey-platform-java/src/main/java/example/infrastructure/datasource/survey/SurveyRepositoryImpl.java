package example.infrastructure.datasource.survey;

import example.domain.model.survey.SurveyRepository;
import example.domain.model.survey.data.validated.Survey;
import example.infrastructure.datasource.survey.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyRepositoryImpl implements SurveyRepository {

    private final JdbcClient client;
    private final JdbcTemplate template;

    @Override
    public void createSurvey(Survey survey) {
        var surveyId = this.saveSurvey(new SurveyDTO(survey));
        this.saveSingleChoiceQuestions(SingleChoiceQuestionDTO.ofList(surveyId, survey.questions()));
        this.saveMultipleChoiceQuestions(MultipleChoiceQuestionDTO.ofList(surveyId, survey.questions()));
        this.saveDescriptionQuestions(DescriptionQuestionDTO.ofList(surveyId, survey.questions()));
        this.saveOptions(OptionDTO.ofList(surveyId, survey.questions()));
    }

    private long saveSurvey(SurveyDTO surveyDTO) {
        var sql = """
                insert into survey (survey_name, term_from, term_to)
                    values (?, ?, ?)
                """;
        return this.client.sql(sql)
                .param(1, surveyDTO.getSurveyName())
                .param(2, surveyDTO.getFrom())
                .param(3, surveyDTO.getTo())
                .update(new GeneratedKeyHolder(), "survey_id");
    }

    private void saveSingleChoiceQuestions(List<SingleChoiceQuestionDTO> questionDTOs) {
        var sql = """
                insert into single_choice_question (survey_id, question_ordinal, question_sentence)
                    values (?, ?, ?)
                """;
        this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var dto = (SingleChoiceQuestionDTO) questionDTOs.get(i);
                ps.setLong(1, dto.getSurveyId());
                ps.setShort(2, dto.getQuestionOrdinal());
                ps.setString(3, dto.getQuestionSentence());
            }

            @Override
            public int getBatchSize() {
                return questionDTOs.size();
            }
        });
    }

    private void saveMultipleChoiceQuestions(List<MultipleChoiceQuestionDTO> questionDTOs) {
        var sql = """
                insert into multiple_choice_question (survey_id, question_ordinal, question_sentence)
                    values (?, ?, ?)
                """;
        this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var dto = (MultipleChoiceQuestionDTO) questionDTOs.get(i);
                ps.setLong(1, dto.getSurveyId());
                ps.setShort(2, dto.getQuestionOrdinal());
                ps.setString(3, dto.getQuestionSentence());
            }

            @Override
            public int getBatchSize() {
                return questionDTOs.size();
            }
        });
    }

    private void saveDescriptionQuestions(List<DescriptionQuestionDTO> questionDTOs) {
        var sql = """
                insert into multiple_choice_question (survey_id, question_ordinal, question_sentence)
                    values (?, ?, ?)
                """;
        this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var dto = (DescriptionQuestionDTO) questionDTOs.get(i);
                ps.setLong(1, dto.getSurveyId());
                ps.setShort(2, dto.getQuestionOrdinal());
                ps.setString(3, dto.getQuestionSentence());
            }

            @Override
            public int getBatchSize() {
                return questionDTOs.size();
            }
        });
    }

    private void saveOptions(List<OptionDTO> optionDTOs) {
        var sql = """
                insert into option (survey_id, question_ordinal, option_ordinal, option_sentence)
                    values (?, ?, ?, ?)
                """;
        this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, optionDTOs.get(i).getSurveyId());
                ps.setShort(2, optionDTOs.get(i).getQuestionOrdinal());
                ps.setShort(3, optionDTOs.get(i).getOptionOrdinal());
                ps.setString(4, optionDTOs.get(i).getOptionSentence());
            }

            @Override
            public int getBatchSize() {
                return optionDTOs.size();
            }
        });
    }
}
