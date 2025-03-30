package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection // ✅ Helps Quarkus detect this class at runtime
@JsonIgnoreProperties(ignoreUnknown = true)
data class LeagueSimple(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("name") val name: String? = "",
    @JsonProperty("nickname") val short_name: String? = "",
    @JsonProperty("origin") val origin: Int? = null,
    @JsonProperty("region") val region: String? = null
)
