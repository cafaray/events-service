package main.kotlin.com.supplier.championleague.service

import main.kotlin.com.supplier.championleague.model.serializer.VenueResponse
import main.kotlin.com.supplier.championleague.repositories.VenueRepository
import main.kotlin.com.supplier.championleague.repositories.VenueSearchPosRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class VenueServiceTest {

    private lateinit var venueRepository: VenueRepository
    private lateinit var venueSearchPosRepository: VenueSearchPosRepository
    private lateinit var venueService: VenueService

    @BeforeEach
    fun setUp() {
        venueRepository = mock(VenueRepository::class.java)
        venueSearchPosRepository = mock(VenueSearchPosRepository::class.java)
        venueService = VenueService(venueRepository, venueSearchPosRepository)
    }

    @Test
    fun `getVenues should return venues from repository`() {
        val mockVenues = arrayListOf<Map<String, Any>>(mapOf("id" to "1"))
        `when`(venueRepository.getVenues()).thenReturn(mockVenues)

        val result = venueService.getVenues()

        assertEquals(mockVenues, result)
        verify(venueRepository).getVenues()
    }

    @Test
    fun `getVenueByPosition should return nearby venues with default radius`() {
        val mockVenues = listOf<VenueResponse>()
        `when`(venueSearchPosRepository.getNearby(1.0, 2.0, 50)).thenReturn(mockVenues)

        val result = venueService.getVenueByPosition(1.0, 2.0)

        assertEquals(mockVenues, result)
        verify(venueSearchPosRepository).getNearby(1.0, 2.0, 50)
    }

    @Test
    fun `findNearby should return venues within specified distance`() {
        val mockVenues = listOf<VenueResponse>()
        `when`(venueSearchPosRepository.getNearby(1.0, 2.0, 100)).thenReturn(mockVenues)

        val result = venueService.findNearby(1.0, 2.0, 100)

        assertEquals(mockVenues, result)
        verify(venueSearchPosRepository).getNearby(1.0, 2.0, 100)
    }
}