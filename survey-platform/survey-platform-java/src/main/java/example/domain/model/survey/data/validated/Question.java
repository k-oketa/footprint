package example.domain.model.survey.data.validated;

public sealed interface Question permits
    SingleChoiceQuestion,
    MultipleChoiceQuestion,
    DescriptionQuestion {
}
