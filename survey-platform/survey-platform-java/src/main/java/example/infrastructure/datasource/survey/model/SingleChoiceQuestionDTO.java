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
public class SingleChoiceQuestionDTO {

    private long surveyId;
    private short questionOrdinal;
    private String questionSentence;

    public static List<SingleChoiceQuestionDTO> ofList(long surveyId, List<Question> questions) {
        return IntStream.range(0, questions.size())
                .filter(ordinal -> questions.get(ordinal) instanceof SingleChoiceQuestion)
                .mapToObj(ordinal -> switch (questions.get(ordinal)) {
                    case SingleChoiceQuestion singleChoiceQuestion -> {
                        var questionSentence = singleChoiceQuestion.questionSentence().value();
                        yield new SingleChoiceQuestionDTO(surveyId, (short) ordinal, questionSentence);
                    }
                    case DescriptionQuestion ignored -> null;
                    case MultipleChoiceQuestion ignored -> null;
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
