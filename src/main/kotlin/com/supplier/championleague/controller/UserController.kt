package main.kotlin.com.supplier.championleague.controller

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import main.kotlin.com.supplier.championleague.model.User
import main.kotlin.com.supplier.championleague.service.UserService

@Path("/v1/users")
@Produces(MediaType.APPLICATION_JSON)
class UserController(private val userService: UserService){

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addUser(user: User): Response {
        println("initiating post action for user: ${user.name}")
        val uid = user.id ?: return Response.status(400).entity("Missing UID").build()
        val name = user.name ?: return Response.status(400).entity("Missing Name").build()
        val email = user.email ?: return Response.status(400).entity("Missing eMail").build()
        println("firestoreService.addUser(${uid}, ${name}, ${email})")
        userService.postUser(uid, name, email)
        return Response.ok(mapOf("message" to "User added successfully")).build()
    }

    @GET
    fun getUsers(): Response {
        val users = userService.getUsers()
        return if (users!!.size>0) {
            Response.ok(users).build()
        } else {
            Response.status(404).entity(mapOf("error" to "User not found")).build()
        }
    }

    @GET
    @Path("/{uid}")
    fun getUser(@PathParam("uid") uid: String): Response {
        val userData = userService.getUser(uid)
        return if (userData != null) {
            Response.ok(userData).build()
        } else {
            Response.status(404).entity(mapOf("error" to "User not found")).build()
        }
    }
}