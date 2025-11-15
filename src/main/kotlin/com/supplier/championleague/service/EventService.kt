package com.supplier.championleague.service

import com.supplier.championleague.repositories.EventRepository
import com.supplier.championleague.repositories.EventQueryRepository
import main.kotlin.com.supplier.championleague.repositories.EventSongRepository
import main.kotlin.com.supplier.championleague.repositories.EventMatchRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class EventService (
    val eventRepository: EventRepository,
    val eventSongRepository: EventSongRepository,
    val eventQueryRepository: EventQueryRepository,
    val eventMatchRepository: EventMatchRepository
) {

    fun getEvents(
        type: String?,
        name: String?,
        date: String?,
        status: String?,        
        lat: Double?,
        long: Double?
    ): ArrayList<Map<String, Any>?>? = eventRepository.getAllEvents(type, name, date, status, lat, long)


    fun getEventById(
        type: String?,
        event_id: String?
    ): ArrayList<Map<String, Any>?>? = eventRepository.getEventById(type, event_id)

    fun saveEventSongAttendee(
        eventSongId: String,
        userId: String,
        details: ArrayList<Map<String, Any>>
    ): String? {
        // Validate that details contains at least one map with token key
        val hasToken = details.any { map ->
            map.containsKey("token")
        }
        
        if (!hasToken) {
            throw IllegalArgumentException("Details must contain at least one map with 'token' key")
        }
        
        return eventSongRepository.saveAttendee(eventSongId, userId, details)
    }

    fun getEventSongs(name: String?,
        date: String?,
        status: String?) : ArrayList<Map<String, Any>>? = eventSongRepository.getEventSongs(name, date, status, null, null)
        ?: throw Exception("No event songs found")


    fun getEventSongById(eventSongId: String): Map<String, Any>? {
        val eventSong = eventSongRepository.getEventSong(eventSongId)
        return eventSong ?: throw Exception("Event song with ID $eventSongId not found")
    }

    fun saveEventQueryAttendee(
        eventQueryId: String,
        userId: String
    ): String? {
        if (eventQueryId.isBlank()) {
            throw IllegalArgumentException("EventQueryId cannot be blank")
        }
        if (userId.isBlank()) {
            throw IllegalArgumentException("UserId cannot be blank")
        }
        
        return eventQueryRepository.saveEventQueryAttendee(eventQueryId, userId)
    }

    fun saveEventQueryAnswers(
        eventQueryId: String,
        userId: String,
        answers: List<Map<String, Any>>
    ): String? {
        if (eventQueryId.isBlank()) {
            throw IllegalArgumentException("EventQueryId cannot be blank")
        }
        if (userId.isBlank()) {
            throw IllegalArgumentException("UserId cannot be blank")
        }
        if (answers.isEmpty()) {
            throw IllegalArgumentException("Answers cannot be empty")
        }
        
        // Validate each answer has required fields
        answers.forEach { answer ->
            if (!answer.containsKey("questionId") || !answer.containsKey("answer")) {
                throw IllegalArgumentException("Each answer must contain 'questionId' and 'answer' fields")
            }
        }
        
        return eventQueryRepository.saveEventQueryAnswers(eventQueryId, userId, answers)
    }

    fun saveEventMatchAttendee(
        eventMatchId: String,
        userId: String,
        geoposition: Map<String, Any>
    ): String? {
        if (eventMatchId.isBlank()) {
            throw IllegalArgumentException("EventMatchId cannot be blank")
        }
        if (userId.isBlank()) {
            throw IllegalArgumentException("UserId cannot be blank")
        }
        if (!geoposition.containsKey("lat") || !geoposition.containsKey("lng")) {
            throw IllegalArgumentException("Geoposition must contain 'lat' and 'lng' fields")
        }
        
        return eventMatchRepository.saveEventMatchAttendee(eventMatchId, userId, geoposition)
    }

    fun updateEventMatchAttendeeDetails(
        eventMatchId: String,
        userId: String,
        geoposition: Map<String, Any>
    ): String? {
        if (eventMatchId.isBlank()) {
            throw IllegalArgumentException("EventMatchId cannot be blank")
        }
        if (userId.isBlank()) {
            throw IllegalArgumentException("UserId cannot be blank")
        }
        if (!geoposition.containsKey("lat") || !geoposition.containsKey("lng") || !geoposition.containsKey("accuracy")) {
            throw IllegalArgumentException("Geoposition must contain 'lat', 'lng', and 'accuracy' fields")
        }
        
        val lat = geoposition["lat"]
        val lng = geoposition["lng"]
        val accuracy = geoposition["accuracy"]
        
        if (lat !is Number || lng !is Number || accuracy !is Number) {
            throw IllegalArgumentException("Lat, lng, and accuracy must be numeric values")
        }
        
        return eventMatchRepository.updateEventMatchAttendeeDetails(eventMatchId, userId, geoposition)
            ?: throw IllegalArgumentException("Attendee not found for the specified event match and user")
    }

    /***
     * Depreacated
     * ... 
    fun getEventsByQuery(
        lat: Double?, long: Double?, date: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>?>? = eventRepository.queryEvents(date=date, lat=lat, long=long, limit=limit, offset=offset)
    */
}