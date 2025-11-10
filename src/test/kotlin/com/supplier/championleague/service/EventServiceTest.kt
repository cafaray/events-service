package com.supplier.championleague.service

import com.supplier.championleague.repositories.EventRepository
import main.kotlin.com.supplier.championleague.repositories.EventSongRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class EventServiceTest {

    private lateinit var eventRepository: EventRepository
    private lateinit var eventSongRepository: EventSongRepository
    private lateinit var eventService: EventService

    @BeforeEach
    fun setUp() {
        eventRepository = mock(EventRepository::class.java)
        eventSongRepository = mock(EventSongRepository::class.java)
        eventService = EventService(eventRepository, eventSongRepository)
    }

    @Test
    fun `getEvents should return events from repository`() {
        val mockEvents = arrayListOf<Map<String, Any>?>(mapOf("id" to "1", "name" to "Test Event"))
        `when`(eventRepository.getAllEvents("type", "name", "date", "status", 1.0, 2.0))
            .thenReturn(mockEvents)

        val result = eventService.getEvents("type", "name", "date", "status", 1.0, 2.0)

        assertEquals(mockEvents, result)
        verify(eventRepository).getAllEvents("type", "name", "date", "status", 1.0, 2.0)
    }

    @Test
    fun `getEventById should return event from repository`() {
        val mockEvents = arrayListOf<Map<String, Any>?>(mapOf("id" to "1"))
        `when`(eventRepository.getEventById("type", "eventId")).thenReturn(mockEvents)

        val result = eventService.getEventById("type", "eventId")

        assertEquals(mockEvents, result)
        verify(eventRepository).getEventById("type", "eventId")
    }

    @Test
    fun `saveEventSongAttendee should save when details contain token`() {
        val details = arrayListOf<Map<String, Any>>(mapOf("token" to "abc123"))
        `when`(eventSongRepository.saveAttendee("songId", "userId", details)).thenReturn("attendeeId")

        val result = eventService.saveEventSongAttendee("songId", "userId", details)

        assertEquals("attendeeId", result)
        verify(eventSongRepository).saveAttendee("songId", "userId", details)
    }

    @Test
    fun `saveEventSongAttendee should throw exception when no token in details`() {
        val details = arrayListOf<Map<String, Any>>(mapOf("other" to "value"))

        assertThrows(IllegalArgumentException::class.java) {
            eventService.saveEventSongAttendee("songId", "userId", details)
        }
    }
}