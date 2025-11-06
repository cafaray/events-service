package main.kotlin.com.supplier.championleague.controller

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import main.kotlin.com.supplier.championleague.service.SongService


@Path("/v1/songs")
@Produces(MediaType.APPLICATION_JSON)
class SongController (private val songService: SongService) {

    @GET
    fun getSongs(): Response {
        val songs = songService.getSongs()
        return if (!songs.isNullOrEmpty()){
            Response.ok(songs).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Songs don't found")).build()
        }
    }
    @GET
    @Path("/{uid}")
    fun getSong(@PathParam("uid") uid:String): Response {
        val songData = songService.getSong(uid)
        return if (songData!=null){
            Response.ok(songData).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Song not found")).build()
        }
    }
}