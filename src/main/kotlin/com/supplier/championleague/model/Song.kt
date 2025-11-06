package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class Song (
    @JsonProperty("id") var id: String? = null,
    @JsonProperty("name") val name: String? = null,  
    @JsonProperty("songFile") val song_file: String? = null,  
    @JsonProperty("duration") val duration: Int? = null,
)