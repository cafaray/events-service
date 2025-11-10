package com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class Question(
    @JsonProperty("type") val type: QuestionType,
    @JsonProperty("questionText") val questionText: String,
    @JsonProperty("questionClass") val questionClass: QuestionClass,
    @JsonProperty("options") val options: List<String>? = null,
    @JsonProperty("correctAnswer") val correctAnswer: Any? = null
)

enum class QuestionType {
    MULTISELECT, TEXT, TRUE_FALSE
}
enum class QuestionClass {
    TEAM, PLAYER, VENUE, MATCH, LEAGUE
}