package com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
    @JsonProperty("id") val event_id: String = "",
    @JsonProperty("date") val date: String = "",
    @JsonProperty("desription") val description: String = "",    
    @JsonProperty("name") val name: String = "",
    @JsonProperty("status") val status: String = "",
    @JsonProperty("type") val type: String = EventType.MATCH.toString(),
    @JsonProperty("details") val details: Any?
)

enum class EventType {
    MATCH, QUERY, CAST, SONG
}