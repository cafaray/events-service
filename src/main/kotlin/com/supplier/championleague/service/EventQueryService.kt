package com.supplier.championleague.service

import jakarta.enterprise.context.ApplicationScoped
import com.supplier.championleague.repositories.EventQueryRepository

@ApplicationScoped
class EventQueryService(val eventQueryRepository: EventQueryRepository) {

    fun getEventQueries(name: String?, date: String?, status: String?, limit: Int? = 25, offset: Int? = 1): ArrayList<Map<String, Any>>? {
        return eventQueryRepository.getEventQueries(name, date, status, limit, offset)
    }

    fun getEventQuery(uid: String): Map<String, Any>? {
        return eventQueryRepository.getEventQuery(uid)
    }

    fun getEventQueryDetails(uid: String): Map<String, Any>? {
        return eventQueryRepository.getEventQueryDetails(uid)
    }
}