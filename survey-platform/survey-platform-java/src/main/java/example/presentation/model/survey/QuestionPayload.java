package example.presentation.model.survey;

import example.domain.model.survey.data.unvalidated.UnvalidatedQuestion;
import example.domain.model.survey.data.unvalidated.UnvalidatedQuestionType;
import lombok.Data;

import java.util.List;

@Data
public class QuestionPayload {
    private String questionSentence;
    private String questionType;
    private List<String> options;

    public UnvalidatedQuestion convert() throws IllegalArgumentException {
        var questionType = UnvalidatedQuestionType.valueOf(this.questionType);
        return new UnvalidatedQuestion(questionType, this.questionSentence, this.options);
    }
}
