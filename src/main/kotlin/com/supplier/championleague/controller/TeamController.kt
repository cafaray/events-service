package main.kotlin.com.supplier.championleague.controller

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import main.kotlin.com.supplier.championleague.repositories.TeamRepository

@ApplicationScoped
@Path("/v1/teams")
@Consumes(MediaType.APPLICATION_JSON)
class TeamController (private val teamRepository: TeamRepository) {

    @GET
    fun getTeams(): Response{
        val teams = teamRepository.getTeams()
        return if (teams!!.size>0){
            Response.ok(teams).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Teams not found")).build()
        }
    }

    @GET
    @Path("/{id}")
    fun getTeams(@PathParam("id")uid:String): Response{
        val team = teamRepository.getTeam(uid)
        return if (team!=null){
            Response.ok(team).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Teams not found")).build()
        }
    }
}