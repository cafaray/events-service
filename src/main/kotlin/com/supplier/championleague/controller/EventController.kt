package com.supplier.championleague.controller

import com.supplier.championleague.service.EventService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.PathParam

@Path("/v1/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EventController (private val eventService: EventService) {

    @GET
    fun getEvents(
        @QueryParam("type") type: String?,
        @QueryParam("name") name: String?,
        @QueryParam("date") date: String?,
        @QueryParam("status") status: String?,        
        @QueryParam("lat") lat: Double?,
        @QueryParam("long") long: Double?,        
    ): Response {
        val events = eventService.getEvents(type, name, date, status, lat, long)
        println("controller -> events-fetched: ${events?.size}")
        return if(events!!.size> 0){
            Response.ok(events).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Events not found")).build()
        }
    }
    @GET
    @Path("/{type}")
    fun getEventsByType(
        @PathParam("type") type: String,
        @QueryParam("name") name: String?,
        @QueryParam("date") date: String?,
        @QueryParam("status") status: String?,        
        @QueryParam("lat") lat: Double?,
        @QueryParam("long") long: Double?,        
    ): Response {
        val events = eventService.getEvents(type, name, date, status, lat, long)
        if (events != null){
            println("controller -> events-fetched by type: ${events.size}")
        } else {
            println("controller -> no events fetched by type")
            return Response.status(404).entity(mapOf("error" to "Events not found")).build()
        }
        return if(events!!.size> 0){
            Response.ok(events).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Events not found")).build()
        }
    }

    @GET
    @Path("/{type}/{event_id}")
    fun getEventsByType(
        @PathParam("type") type: String,
        @PathParam("event_id") event_id: String
        ): Response {
        val events = eventService.getEventById(type, event_id)
        return if(events!!.size> 0){
            Response.ok(events).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Event not found")).build()
        }
    }

    /***
     * deprecated
     */
    @GET
    @Path("/inqueries")
    fun getEventInqueries(
        @QueryParam("lat") lat: Double?,
        @QueryParam("long") long: Double?,
        @QueryParam("date") date: String?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response {
        return Response.status(407).entity(mapOf("error" to "Deprecated function. Use /events instead")).build()
        // val events = eventService.getEventsByQuery(lat, long, date, limit, offset)
        // return if(events!!.size> 0){
        //    Response.ok(events).build()
        // } else {
        //    Response.status(404).entity(mapOf("error" to "Events not found")).build()
        // }
    }

}