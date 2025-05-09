package com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection // ✅ Helps Quarkus detect this class at runtime
@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
    @JsonProperty("id") val event_id: String = "",
    @JsonProperty("type") val type: String = EventType.MATCH.toString(),
    @JsonProperty("name") val name: String = "",
    @JsonProperty("date") val date: String = "",
    @JsonProperty("details") val details: Any?
)

enum class EventType {
    MATCH, QUERY, NARROWCAST, SONG, POLL, POST
}