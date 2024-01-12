
drop schema if exists public cascade;
create schema public;

create table survey (
    survey_id bigserial primary key,
    survey_name text not null,
    term_from date not null,
    term_to date not null
);

create table single_choice_question (
    survey_id bigint not null,
    question_ordinal smallint not null,
    question_sentence text not null,
    primary key (survey_id, question_ordinal),
    foreign key (survey_id) references survey(survey_id)
);

create table multiple_choice_question (
    survey_id bigint not null,
    question_ordinal smallint not null,
    question_sentence text not null,
    primary key (survey_id, question_ordinal),
    foreign key (survey_id) references survey(survey_id)
);

create table description_question (
    survey_id bigint not null,
    question_ordinal smallint not null,
    question_sentence text not null,
    primary key (survey_id, question_ordinal),
    foreign key (survey_id) references survey(survey_id)
);

create table option (
    survey_id bigint not null,
    question_ordinal smallint not null,
    option_ordinal smallint not null,
    option_sentence text,
    primary key (survey_id, question_ordinal, option_ordinal),
    foreign key (survey_id) references survey(survey_id)
);