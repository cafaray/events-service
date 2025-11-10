package com.supplier.championleague.service

import com.supplier.championleague.repositories.EventRepository
import main.kotlin.com.supplier.championleague.repositories.EventSongRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class EventService (
    val eventRepository: EventRepository,
    val eventSongRepository: EventSongRepository
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

    /***
     * Depreacated
     * ... 
    fun getEventsByQuery(
        lat: Double?, long: Double?, date: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>?>? = eventRepository.queryEvents(date=date, lat=lat, long=long, limit=limit, offset=offset)
    */
}