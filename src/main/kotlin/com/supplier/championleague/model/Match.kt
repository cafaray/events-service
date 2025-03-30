package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class Match(
    @JsonProperty("id") var id: String? = null,
    @JsonProperty("date") val date: String? = null,
    // @JsonProperty("venue") val venue: Stadium? = null,
    // @JsonProperty("local") val team_local: Team? = null,
    // @JsonProperty("visitor") val team_visitor: Team? = null,
    @JsonProperty("start") val start_time: String? = null,
    @JsonProperty("end") val end_time: String? = null,
    @JsonProperty("winner") val winner: Team? = null,
    @JsonProperty("goals") val goals: Map<String, Any>? = null

)

val objectMapper = ObjectMapper()

fun Any.toMap(): Map<String, Any> {
    return objectMapper.convertValue(this, Map::class.java) as Map<String, Any>
}