package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class EventMatch (
    @JsonProperty("id") var id: String? = null,
    @JsonProperty("date") val date: String? = null,  
    @JsonProperty("description") val description: String? = null,  
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("league_id") val league_id: String? = null,
    @JsonProperty("venue_id") val venue_id: String? = null,
    @JsonProperty("match_id") val match_id: String? = null,
    @JsonProperty("status") val status: String? = null,
    @JsonProperty("time_ending") val time_ending: String? = null,
    @JsonProperty("time_starting") val time_starting: String? = null,
    @JsonProperty("type") val type: String? = null,
    @JsonProperty("team_id_local") val team_id_local: String? = null,
    @JsonProperty("events") val events: ArrayList<Map<String, Any>>? = null
    
)
/*
val objectMapper = ObjectMapper()

fun Any.toMap(): Map<String, Any> {
    return objectMapper.convertValue(this, Map::class.java) as Map<String, Any>
}
*/