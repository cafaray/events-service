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
    @JsonProperty("location") val location: String?= "",
    @JsonProperty("name") val name: String? = "",
    @JsonProperty("nicknames") val nickname: Array<String>? = null,
    @JsonProperty("shortname") val short_name: Array<String>? = null,
    @JsonProperty("stadium") val stadiumId: String? = null,
    @JsonProperty("origin") val origin: Int? = null,
)

