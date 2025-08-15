package com.supplier.championleague.repositories
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import com.supplier.championleague.model.serializer.VenueResponse
import org.jboss.resteasy.reactive.RestResponse;

@Path("/venue-searchpos-service")
@RegisterRestClient(configKey = "venue-searchpos-api")
interface VenueSearchPosRestClient {
    @GET
    @Path("/nearby")
    @Produces(MediaType.APPLICATION_JSON)
    fun getNearby(
        @QueryParam("lat") lat: Double,
        @QueryParam("long") long: Double,
        @QueryParam("maxDistance") maxDistance: Int
    ): RestResponse<List<VenueResponse>>
    // List<Map<String, Any>>?  Adjusted to match the expected return type
    // List<VenueResponse>
}