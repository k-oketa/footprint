package example.domain.model.survey.data.validated;

import java.util.List;

public record MultipleChoiceQuestion(
        QuestionSentence questionSentence,
        List<Option> options) implements Question {
}
