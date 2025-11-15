package com.supplier.championleague.service

import com.supplier.championleague.repositories.EventQueryRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class EventQueryServiceTest {

    private lateinit var eventQueryRepository: EventQueryRepository
    private lateinit var eventQueryService: EventQueryService

    @BeforeEach
    fun setUp() {
        eventQueryRepository = mock(EventQueryRepository::class.java)
        eventQueryService = EventQueryService(eventQueryRepository)
    }

    @Test
    fun `getEventQueries should return queries from repository`() {
        val mockQueries = arrayListOf<Map<String, Any>>(mapOf("id" to "1"))
        `when`(eventQueryRepository.getEventQueries(25, 1)).thenReturn(mockQueries)

        val result = eventQueryService.getEventQueries(25, 1)

        assertEquals(mockQueries, result)
        verify(eventQueryRepository).getEventQueries(25, 1)
    }

    @Test
    fun `getEventQuery should return query from repository`() {
        val mockQuery = mapOf("id" to "1")
        `when`(eventQueryRepository.getEventQuery("uid")).thenReturn(mockQuery)

        val result = eventQueryService.getEventQuery("uid")

        assertEquals(mockQuery, result)
        verify(eventQueryRepository).getEventQuery("uid")
    }

    @Test
    fun `getEventQueryDetails should return query details from repository`() {
        val mockDetails = mapOf("id" to "1", "details" to "test")
        `when`(eventQueryRepository.getEventQueryDetails("uid")).thenReturn(mockDetails)

        val result = eventQueryService.getEventQueryDetails("uid")

        assertEquals(mockDetails, result)
        verify(eventQueryRepository).getEventQueryDetails("uid")
    }
}