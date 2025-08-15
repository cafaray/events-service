package com.supplier.championleague.model.serializer
import kotlinx.serialization.Serializable

@Serializable
data class VenueResponse(
    val name: String,
    // val address: String,
    // val distance: Double,
    val capacity: Int,
    val city: String,
    val coat_of_arms: String? = null,
    val country: String,
    val founded: String? = null,
    // val _id: String? = null
)