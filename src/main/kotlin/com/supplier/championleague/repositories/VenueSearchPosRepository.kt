package com.supplier.championleague.repositories

import com.supplier.championleague.model.serializer.VenueResponse
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
        println("Fetching nearby venues for lat: $lat, long: $long, maxDistance: $maxDistance")

        return try {            
            // Call REST client
            val response = httpClient.getNearby(lat, long, maxDistance)
            println("StatusCode: ${response.statusInfo}")
            println("entity: ${response.getEntity()}")
            // println("Body: {resposne.body}")
            val rawVenues = response.getEntity() ?: emptyList()
            println("Body: ${rawVenues}")
            
            // Map to VenueResponse
            // rawVenues.map { raw ->
            //    VenueResponse(
            //        name = raw.name ?: "Unknown",
            //        capacity = raw.capacity ?: 0,
            //        city = raw.city ?: "Unknown",
            //        coat_of_arms = raw.coat_of_arms,
            //        country = raw.country ?: "Unknown",
            //        founded = raw.founded,
            //        _id = raw._id
            //    )
            // }
            // emptyList()
            // Debug print
            println("✅ Received ${rawVenues.size} venues from remote service")
            rawVenues.forEachIndexed { index, venue ->
                println("[$index] Raw venue: $venue")
            }
            rawVenues
            // Map to VenueResponse
            //rawVenues.map { raw ->
            //    VenueResponse(
            //        name = raw.name ?: "Unknown",
            //        capacity = raw.capacity ?: 0,
            //        city = raw.city ?: "Unknown",
            //        coat_of_arms = raw.coat_of_arms,
            //        country = raw.country ?: "Unknown",
            //        founded = raw.founded,
            //        _id = raw._id
            //    )
            
        } catch (e: ClientWebApplicationException) {
            System.err.println("❌ Error calling VenueSearch API")
            e.printStackTrace()

            emptyList()
        } catch (e: Exception) {
            System.err.println("❌ Unexpected error: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    
        


        // return httpClient.getNearby(lat, long, maxDistance)
    }
}