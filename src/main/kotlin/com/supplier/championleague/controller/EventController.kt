package com.supplier.championleague.controller

import com.supplier.championleague.service.EventService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@ApplicationScoped
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
class EventController (private val eventService: EventService) {

    @GET
    fun getEvents(): Response {
        val events = eventService.getEvents()
        return if(events!!.size> 0){
            Response.ok(events).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Events not found")).build()
        }
    }

    @GET
    @Path("/inqueries")
    fun getEventInqueries(
        @QueryParam("lat") lat: Double?,
        @QueryParam("long") long: Double?,
        @QueryParam("date") date: String?,
        // @QueryParam("team") team: String?,
        // @QueryParam("venue") venue: String?,
        // @QueryParam("league") league: String?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response {
        val events = eventService.getEventsByQuery(lat, long, date, limit, offset)
        return if(events!!.size> 0){
            Response.ok(events).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Events not found")).build()
        }
    }

}