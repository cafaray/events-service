package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class MatchDetailed(
    @JsonProperty("id") var id: String? = null,    
    @JsonProperty("team_id_1") val team_id_1: Map<String, Any>? = null,
    @JsonProperty("team_id_2") val team_id_2: Map<String, Any>? = null    
)

val objectMapper = ObjectMapper()

fun Any.toMap(): Map<String, Any> {
    return objectMapper.convertValue(this, Map::class.java) as Map<String, Any>
}
