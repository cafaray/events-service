package com.supplier.championleague.service

import com.supplier.championleague.repositories.EventRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class EventService (val eventRepository: EventRepository) {

    fun getEvents(): ArrayList<Map<String, Any>?>? = eventRepository.getAllEvents()

    fun getEventsByQuery(
        lat: Double?, long: Double?, date: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>?>? = eventRepository.queryEvents(lat, long, date, limit, offset)

}