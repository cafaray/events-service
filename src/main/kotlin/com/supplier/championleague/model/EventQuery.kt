package com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class EventQuery(
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String? = null,
    @JsonProperty("date") val date: String,
    @JsonProperty("type") val type: String = "QUERY",
    @JsonProperty("status") val status: EventStatus,
    @JsonProperty("questionaire_id") val questionaireId: String,
    @JsonProperty("time_to_answer") val timeToAnswer: Int
)

enum class EventStatus {
    UPCOMING, LIVE, FINISHED, CANCELLED
}