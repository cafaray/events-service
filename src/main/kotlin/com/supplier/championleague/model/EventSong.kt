package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class EventSong (
    @JsonProperty("id") var id: String? = null,
    @JsonProperty("date") val date: String? = null,  
    @JsonProperty("description") val description: String? = null,  
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("status") val status: String? = null,
    @JsonProperty("song_id") val song_id: String? = null,
    @JsonProperty("start_at_ms") val start_at: Long? = null,
    @JsonProperty("start_in_ms") val start_in: Long? = null,
    @JsonProperty("team_id") val team_id: String? = null, 
    @JsonProperty("type") val type: String? = "SONG", 
)