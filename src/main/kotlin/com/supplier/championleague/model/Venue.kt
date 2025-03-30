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
    @JsonProperty("country") val country: String? = "",
    @JsonProperty("surface") val surface: String? = "",
    @JsonProperty("image") val image: URL? = null
)
