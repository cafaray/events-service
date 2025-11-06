package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import java.net.URL

@RegisterForReflection // Helps Quarkus detect this class at runtime
@JsonIgnoreProperties(ignoreUnknown = true)
data class Venue(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("address") val address: String? = "",
    @JsonProperty("capacity") val capacity: Int? =0,
    @JsonProperty("city") val city: String? = "",
    @JsonProperty("club") val club: String? = "",
    @JsonProperty("country") val country: String? = "",
    @JsonProperty("image") val image: URL? = null,
    @JsonProperty("name") val name: String? = "",
    @JsonProperty("nickname") val nickname: String? = "",
    @JsonProperty("foundation") val foundation: Int?,
    @JsonProperty("surface") val surface: String? = "",
)
