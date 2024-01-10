package example.domain.model.survey.data.unvalidated;

import java.util.List;

public record UnvalidatedQuestion(
        UnvalidatedQuestionType questionType,
        String questionSentence,
        List<String> options) {
}
