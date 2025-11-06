package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection // ✅ Helps Quarkus detect this class at runtime
@JsonIgnoreProperties(ignoreUnknown = true)
data class TeamSimple(
    @JsonProperty("id") var id: String? = "",
    @JsonProperty("crest") var crest: String? = "",
    @JsonProperty("country") val country: String? = "",
    @JsonProperty("location") val location: String?= "",
    @JsonProperty("name") val name: String? = "",    
    @JsonProperty("shortName") val short_name: String? = null,
)
