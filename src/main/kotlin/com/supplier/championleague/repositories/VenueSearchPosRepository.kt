//package com.supplier.championleague.repositories
package main.kotlin.com.supplier.championleague.repositories
import main.kotlin.com.supplier.championleague.model.serializer.VenueResponse
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.rest.client.inject.RestClient
import jakarta.ws.rs.core.UriBuilder
import java.net.URI
import jakarta.inject.Inject
import org.jboss.resteasy.reactive.ClientWebApplicationException
import org.jboss.resteasy.reactive.RestResponse;

@ApplicationScoped
class VenueSearchPosRepository @Inject constructor(
    @RestClient private val httpClient: VenueSearchPosRestClient
) {
    fun getNearby(lat: Double, long: Double, maxDistance: Int): List<VenueResponse> {
        println("** Fetching nearby venues for:\n-> lat: $lat,\n-> long: $long,\n-> maxDistance: $maxDistance")

        return try {            
            val response = httpClient.getNearby(lat, long, maxDistance)
            
            if (response.status >= 400) {
                println("❌ API returned error status: ${response.status}")
                return emptyList()
            }
            
            val rawVenues = response.getEntity() ?: emptyList()
            println("✅ Received ${rawVenues.size} venues from remote service")
            println("✅ Received ${rawVenues} venues from remote service")
            rawVenues.map { raw ->
                VenueResponse(
                    name = raw.name,
                    capacity = raw.capacity,
                    city = raw.city,
                    coat_of_arms = raw.coat_of_arms,
                    country = raw.country,
                    founded = raw.founded,
                    venue_id = raw.venue_id
                )
            }
            
        } catch (e: ClientWebApplicationException) {
            println("❌ VenueSearch API error: ${e.response?.status} - ${e.message}")
            emptyList()
        } catch (e: Exception) {
            println("❌ Unexpected error in venue search: ${e.message}")
            emptyList()
        }

    }
}