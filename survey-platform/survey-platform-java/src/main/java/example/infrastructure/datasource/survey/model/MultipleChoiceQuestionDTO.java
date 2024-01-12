package example.infrastructure.datasource.survey.model;

import example.domain.model.survey.data.validated.DescriptionQuestion;
import example.domain.model.survey.data.validated.MultipleChoiceQuestion;
import example.domain.model.survey.data.validated.Question;
import example.domain.model.survey.data.validated.SingleChoiceQuestion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class MultipleChoiceQuestionDTO {

    private long surveyId;
    private short questionOrdinal;
    private String questionSentence;

    public static List<MultipleChoiceQuestionDTO> ofList(long surveyId, List<Question> questions) {
        return IntStream.range(0, questions.size())
                .mapToObj(ordinal -> switch (questions.get(ordinal)) {
                    case SingleChoiceQuestion ignored -> null;
                    case MultipleChoiceQuestion multipleChoiceQuestion -> {
                        var questionSentence = multipleChoiceQuestion.questionSentence().value();
                        yield new MultipleChoiceQuestionDTO(surveyId, (short) ordinal, questionSentence);
                    }
                    case DescriptionQuestion ignored -> null;
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
