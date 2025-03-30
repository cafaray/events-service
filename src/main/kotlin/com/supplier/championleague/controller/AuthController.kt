package main.kotlin.com.supplier.championleague.controller


import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import main.kotlin.com.supplier.championleague.service.AuthService

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AuthController(private val authService: AuthService) {

    @POST
    @Path("/verify")
    fun verifyToken(request: Map<String, String>): Response {
        val token = request["token"] ?: return Response.status(400).entity("Token required").build()
        val decodedToken = authService.verifyToken(token)
        return if (decodedToken != null) {
            Response.ok(mapOf("uid" to decodedToken.uid)).build()
        } else {
            Response.status(401).entity("Invalid token").build()
        }
    }
}