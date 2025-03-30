package main.kotlin.com.supplier.championleague.controller

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import main.kotlin.com.supplier.championleague.service.VenueService

@Path("/v1/venues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class VenueController (private val venueService: VenueService) {

    @GET
    fun getVenues(): Response {
        try {
            val venues = venueService.getVenues()
            return if (venues != null)
                Response.ok(venues).build()
            else
                Response.status(404).entity(mapOf("error" to "Venues not found")).build()
        } catch (e: Exception) {
            return Response.status(500).entity(mapOf("error" to e.message)).build()
        }
    }

    @GET
    @Path("/positions")
    fun getVenues(@QueryParam("lat") lat:Double, @QueryParam("long") long:Double): Response {
        val venues = venueService.getVenueByPosition(lat, long)
        return if (venues != null)
            Response.ok(venues).build()
        else
            Response.status(404).entity(mapOf("error" to "Venues not found")).build()
    }
}
