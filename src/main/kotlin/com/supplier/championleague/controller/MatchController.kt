package main.kotlin.com.supplier.championleague.controller

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import main.kotlin.com.supplier.championleague.service.MatchService


@Path("/v1/matches")
@Produces(MediaType.APPLICATION_JSON)
class MatchController (private val matchService: MatchService) {

    @GET
    fun getMatches(): Response {
        val matches = matchService.getMatches()
        return if (matches!!.size>0){
            Response.ok(matches).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Matches don't found")).build()
        }
    }
    @GET
    @Path("/{uid}")
    fun getMatch(@PathParam("uid") uid:String): Response {
        val matchData = matchService.getMatch(uid)
        return if (matchData!=null){
            Response.ok(matchData).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Match not found")).build()
        }
    }
    @GET
    @Path("/{uid}/details")
    fun getMatchDetails(@PathParam("uid") uid:String): Response {
        val matchData = matchService.getMatchDetails(uid)
        return if (matchData!=null){
            Response.ok(matchData).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Match not found")).build()
        }
    }
}