package com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import com.supplier.championleague.model.Question

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class Questionnaire(
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("type") val type: QuestionnaireType,
    @JsonProperty("questions") val questions: List<Question>
)

enum class QuestionnaireType {
    TRIVIA, POLL, SURVEY
}