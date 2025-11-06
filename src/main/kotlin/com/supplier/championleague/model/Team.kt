package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection // ✅ Helps Quarkus detect this class at runtime
@JsonIgnoreProperties(ignoreUnknown = true)
data class Team(
    @JsonProperty("id") val id: String? = "",
    @JsonProperty("country") val country: String? = "",
    @JsonProperty("crest") val crest: String? = "",
    @JsonProperty("foundation") val foundation: Int?,
    @JsonProperty("location") val location: String?= "",
    @JsonProperty("name") val name: String? = "",
    @JsonProperty("nickNames") val nickname: Array<String>? = null,
    @JsonProperty("shortName") val short_name: String? = null,
    @JsonProperty("venue") val venue: Map<String, Any>? = null,
    
)