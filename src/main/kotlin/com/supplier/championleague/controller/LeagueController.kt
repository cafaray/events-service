package main.kotlin.com.supplier.championleague.controller

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import main.kotlin.com.supplier.championleague.repositories.LeagueRepository


@ApplicationScoped
@Path("/v1/leagues")
@Produces(MediaType.APPLICATION_JSON)
class LeagueController (private val leagueRepository: LeagueRepository) {

    @GET
    fun getLeagues(): Response {
        val leagues = leagueRepository.getLeagues();
        return if(leagues!!.size>0){
            Response.ok(leagues).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Not found leagues")).build()
        }
    }

}