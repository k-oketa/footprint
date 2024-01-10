package example.domain.model.survey.function;

import example.domain.model.survey.data.unvalidated.UnvalidatedQuestion;
import example.domain.model.survey.data.unvalidated.UnvalidatedSurvey;
import example.domain.model.survey.data.validated.*;
import io.vavr.collection.Seq;
import io.vavr.collection.Traversable;
import io.vavr.control.Either;

import java.util.List;
import java.util.function.Function;

public class ValidateSurvey implements Function<UnvalidatedSurvey, Either<String, Survey>> {
    @Override
    public Either<String, Survey> apply(UnvalidatedSurvey unvalidatedSurvey) {
        return this.validateSurveyName(unvalidatedSurvey).flatMap(
                surveyName -> this.validateSurveyTerm(unvalidatedSurvey).flatMap(
                        surveyTerm -> this.validateQuestions(unvalidatedSurvey).flatMap(
                                questions -> Either.right(new Survey(surveyName, surveyTerm, questions))
                        )
                )
        );
    }

    private Either<String, SurveyName> validateSurveyName(UnvalidatedSurvey unvalidatedSurvey) {
        if (unvalidatedSurvey.surveyName().isBlank()) return Either.left("調査名が空欄です");
        return Either.right(new SurveyName(unvalidatedSurvey.surveyName()));
    }

    private Either<String, SurveyTerm> validateSurveyTerm(UnvalidatedSurvey unvalidatedSurvey) {
        if (unvalidatedSurvey.from().isEqual(unvalidatedSurvey.to())) return Either.left("調査の開始日と終了日が同日です");
        if (unvalidatedSurvey.from().isAfter(unvalidatedSurvey.to())) return Either.left("調査の開始日よりも終了日が先です");
        return Either.right(new SurveyTerm(unvalidatedSurvey.from(), unvalidatedSurvey.to()));
    }

    private Either<String, List<Question>> validateQuestions(UnvalidatedSurvey unvalidatedSurvey) {
        var questions = unvalidatedSurvey.questions();
        var results = questions.stream()
                .map(this::validateQuestion)
                .toList();
        return Either.sequence(results)
                .map(Seq::asJava)
                .mapLeft(Traversable::head);
    }

    private Either<String, Question> validateQuestion(UnvalidatedQuestion unvalidatedQuestion) {
        return validateQuestionSentence(unvalidatedQuestion).flatMap(
                questionSentence -> validateOptions(unvalidatedQuestion).flatMap(
                        options -> Either.right(
                                switch (unvalidatedQuestion.questionType()) {
                                    case SingleChoice -> new SingleChoiceQuestion(questionSentence, options);
                                    case MultipleChoice -> new MultipleChoiceQuestion(questionSentence, options);
                                    case Description -> new DescriptionQuestion(questionSentence);
                                }
                        )
                )
        );
    }

    private Either<String, QuestionSentence> validateQuestionSentence(UnvalidatedQuestion unvalidatedQuestion) {
        if (unvalidatedQuestion.questionSentence().isBlank()) return Either.left("質問文が空欄です");
        return Either.right(new QuestionSentence(unvalidatedQuestion.questionSentence()));
    }

    private Either<String, List<Option>> validateOptions(UnvalidatedQuestion unvalidatedQuestion) {
        var options = unvalidatedQuestion.options();
        var results = options.stream()
                .map(this::validateOption)
                .toList();
        return Either.sequence(results)
                .map(Seq::asJava)
                .mapLeft(Traversable::head);
    }

    private Either<String, Option> validateOption(String option) {
        if (option.isBlank()) return Either.left("選択肢が空欄です");
        return Either.right(new Option(option));
    }

}
