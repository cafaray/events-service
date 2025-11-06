package com.supplier.championleague.service

import com.supplier.championleague.repositories.EventRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class EventService (val eventRepository: EventRepository) {

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

    /***
     * Depreacated
     * ... 
    fun getEventsByQuery(
        lat: Double?, long: Double?, date: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>?>? = eventRepository.queryEvents(date=date, lat=lat, long=long, limit=limit, offset=offset)
    */
}