package com.supplier.championleague.controller

import com.supplier.championleague.service.EventService

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.PathParam
import com.supplier.championleague.service.EventQueryService

@Path("/v1/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EventController (
    private val eventService: EventService,
    private val eventQueryService: EventQueryService
) {

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

    @GET
    @Path("/queries")
    fun getEventQueries(
        @QueryParam("name") name: String?,
        @QueryParam("date") date: String?,
        @QueryParam("status") status: String?,        
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response {
        val eventQueries = eventQueryService.getEventQueries(name, date, status, limit, offset)
        return if (!eventQueries.isNullOrEmpty()) {
            Response.ok(eventQueries).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Event queries not found")).build()
        }
    }

    @GET
    @Path("/queries/{uid}")
    fun getEventQuery(@PathParam("uid") uid: String): Response {
        val eventQuery = eventQueryService.getEventQuery(uid)
        return if (eventQuery != null) {
            Response.ok(eventQuery).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Event query not found")).build()
        }
    }

    @GET
    @Path("/queries/{uid}/details")
    fun getEventQueryDetails(@PathParam("uid") uid: String): Response {
        val eventQueryDetails = eventQueryService.getEventQueryDetails(uid)
        return if (eventQueryDetails != null) {
            Response.ok(eventQueryDetails).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Event query details not found")).build()
        }
    }

    @GET
    @Path("/songs")
    fun getEventSongs(
        @QueryParam("name") name: String?,
        @QueryParam("date") date: String?,
        @QueryParam("status") status: String?
    ): Response {
        val eventSongs = eventService.getEventSongs(name, date, status)
        return if (!eventSongs.isNullOrEmpty()) {
            Response.ok(eventSongs).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Event songs not found")).build()
        }
    }

    @GET
    @Path("/songs/{eventSongId}")
    fun getEventSongById(
        @PathParam("eventSongId") eventSongId: String
    ): Response {
        val eventSong = eventService.getEventSongById(eventSongId)
        return if (eventSong != null) {
            Response.ok(eventSong).build()
        } else {
            Response.status(404).entity(mapOf("error" to "Event song not found")).build()
        }
    }

    @POST
    @Path("/songs/{eventSongId}/attendees/{userId}")
    fun saveEventSongAttendee(
        @PathParam("eventSongId") eventSongId: String,
        @PathParam("userId") userId: String,
        details: ArrayList<Map<String, Any>>
    ): Response {
        return try {
            if (eventSongId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid eventSongId")).build()
            }
            if (userId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid userId")).build()
            }
            
            val result = eventService.saveEventSongAttendee(eventSongId, userId, details)
            if (result != null) {
                Response.status(201).entity(mapOf("message" to "Attendee saved successfully", "id" to result)).build()
            } else {
                Response.status(404).entity(mapOf("error" to "Event song not found")).build()
            }
        } catch (e: IllegalArgumentException) {
            Response.status(400).entity(mapOf("error" to e.message)).build()
        } catch (e: Exception) {
            Response.status(500).entity(mapOf("error" to "Internal server error")).build()
        }
    }

    @POST
    @Path("/queries/{eventQueryId}/attendees/{userId}")
    fun saveEventQueryAttendee(
        @PathParam("eventQueryId") eventQueryId: String,
        @PathParam("userId") userId: String
    ): Response {
        return try {
            if (eventQueryId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid eventQueryId")).build()
            }
            if (userId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid userId")).build()
            }
                    
            val result = eventService.saveEventQueryAttendee(eventQueryId, userId)
            if (result != null) {
                Response.status(201).entity(mapOf("message" to "Event query attendee saved successfully", "id" to result)).build()
            } else {
                Response.status(404).entity(mapOf("error" to "Event query not found")).build()
            }
        } catch (e: IllegalArgumentException) {
            Response.status(400).entity(mapOf("error" to e.message)).build()
        } catch (e: Exception) {
            Response.status(500).entity(mapOf("error" to "Internal server error")).build()
        }
    }

    @PUT
    @Path("/queries/{eventQueryId}/attendees/{userId}/answers")
    fun saveEventQueryAnswers(
        @PathParam("eventQueryId") eventQueryId: String,
        @PathParam("userId") userId: String,
        answers: List<Map<String, Any>>
    ): Response {
        return try {
            if (eventQueryId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid eventQueryId")).build()
            }
            if (userId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid userId")).build()
            }
            
            val result = eventService.saveEventQueryAnswers(eventQueryId, userId, answers)
            if (result != null) {
                Response.status(200).entity(mapOf("message" to "Answers saved successfully", "id" to result)).build()
            } else {
                Response.status(404).entity(mapOf("error" to "Event query attendee not found")).build()
            }
        } catch (e: IllegalArgumentException) {
            Response.status(400).entity(mapOf("error" to e.message)).build()
        } catch (e: Exception) {
            Response.status(500).entity(mapOf("error" to "Internal server error")).build()
        }
    }

    @POST
    @Path("/matches/{eventMatchId}/attendees/{userId}")
    fun saveEventMatchAttendee(
        @PathParam("eventMatchId") eventMatchId: String,
        @PathParam("userId") userId: String,
        geoposition: Map<String, Any>
    ): Response {
        return try {
            if (eventMatchId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid eventMatchId")).build()
            }
            if (userId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid userId")).build()
            }
            
            val result = eventService.saveEventMatchAttendee(eventMatchId, userId, geoposition)
            if (result != null) {
                Response.status(201).entity(mapOf("message" to "Event match attendee saved successfully", "id" to result)).build()
            } else {
                Response.status(404).entity(mapOf("error" to "Event match not found")).build()
            }
        } catch (e: IllegalArgumentException) {
            Response.status(400).entity(mapOf("error" to e.message)).build()
        } catch (e: Exception) {
            Response.status(500).entity(mapOf("error" to "Internal server error")).build()
        }
    }

    @PUT
    @Path("/matches/{eventMatchId}/attendees/{userId}/details")
    fun updateEventMatchAttendeeDetails(
        @PathParam("eventMatchId") eventMatchId: String,
        @PathParam("userId") userId: String,
        geoposition: Map<String, Any>
    ): Response {
        return try {
            if (eventMatchId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid eventMatchId")).build()
            }
            if (userId.isBlank()) {
                return Response.status(400).entity(mapOf("error" to "Invalid userId")).build()
            }
            if (geoposition.isEmpty()) {
                return Response.status(400).entity(mapOf("error" to "Geoposition data is required")).build()
            }
            
            val result = eventService.updateEventMatchAttendeeDetails(eventMatchId, userId, geoposition)
            Response.status(200).entity(mapOf("message" to "Attendee details updated successfully", "id" to result)).build()
        } catch (e: IllegalArgumentException) {
            Response.status(400).entity(mapOf("error" to e.message)).build()
        } catch (e: Exception) {
            Response.status(500).entity(mapOf("error" to "Internal server error: ${e.message}")).build()
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
    }

}