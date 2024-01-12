package example.infrastructure.datasource.survey.model;

import example.domain.model.survey.data.validated.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class OptionDTO {

    private long surveyId;
    private short questionOrdinal;
    private short optionOrdinal;
    private String optionSentence;

    public static List<OptionDTO> ofList(long surveyId, List<Question> questions) {
        return IntStream.range(0, questions.size())
                .mapToObj(questionOrdinal ->
                        switch (questions.get(questionOrdinal)) {
                            case SingleChoiceQuestion singleChoiceQuestion -> convert(surveyId, (short) questionOrdinal, singleChoiceQuestion.options());
                            case MultipleChoiceQuestion multipleChoiceQuestion -> convert(surveyId, (short) questionOrdinal, multipleChoiceQuestion.options());
                            case DescriptionQuestion ignored -> new ArrayList<OptionDTO>();
                        }
                )
                .flatMap(List::stream)
                .toList();
    }

    private static List<OptionDTO> convert(long surveyId, short questionOrdinal, List<Option> options) {
        return IntStream.range(0, options.size())
                .mapToObj(optionOrdinal -> new OptionDTO(surveyId, questionOrdinal, (short) optionOrdinal, options.get(optionOrdinal).value()))
                .toList();
    }
}
