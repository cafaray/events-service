package main.kotlin.com.supplier.championleague.model.serializer
import kotlinx.serialization.Serializable

@Serializable
data class VenueResponse(
/* 
    {
        "_id":"689364ff1490c878c62290ae",
        "capacity":55000,
        "city":"Barcelona",
        "coat_of_arms":"https://cdn.worldvectorlogo.com/logos/olimpic-lluis-companys.svg",
        "country":"Spain",
        "founded":"1927-05-14",
        "location":{
            "coordinates":[[[2.157,41.378],[2.16,41.378],[2.16,41.381],[2.157,41.381],[2.157,41.378]]],
            "type":"Polygon"
            },
        "name":"Olímpic Lluís Companys Stadium"
    }
*/

    val name: String,
    // val address: String,
    // val distance: Double,
    val capacity: Int,
    val city: String,
    val coat_of_arms: String? = null,
    val country: String,
    val founded: String? = null,
    val venue_id: String
)