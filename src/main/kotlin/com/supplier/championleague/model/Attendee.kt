package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
data class Attendee(
    @JsonProperty("id") val id: String? = null, // valid User Id saved into the "users" collection
    @JsonProperty("confirmedAt") val confirmed_at: Long,
    @JsonProperty("recorded") val recorded: Boolean = false,
    @JsonProperty("details") val details: ArrayList<Map<String, Any>> = arrayListOf()
)