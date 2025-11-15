package main.kotlin.com.supplier.championleague.service

import main.kotlin.com.supplier.championleague.repositories.MatchRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class MatchServiceTest {

    private lateinit var matchRepository: MatchRepository
    private lateinit var matchService: MatchService

    @BeforeEach
    fun setUp() {
        matchRepository = mock(MatchRepository::class.java)
        matchService = MatchService(matchRepository)
    }

    @Test
    fun `getMatches should return matches from repository`() {
        val mockMatches = arrayListOf<Map<String, Any>>(mapOf("id" to "1"))
        `when`(matchRepository.getMatches()).thenReturn(mockMatches)

        val result = matchService.getMatches()

        assertEquals(mockMatches, result)
        verify(matchRepository).getMatches()
    }

    @Test
    fun `getMatch should return match from repository`() {
        val mockMatch = mapOf("id" to "1")
        `when`(matchRepository.getMatch("uid")).thenReturn(mockMatch)

        val result = matchService.getMatch("uid")

        assertEquals(mockMatch, result)
        verify(matchRepository).getMatch("uid")
    }

    @Test
    fun `getMatchDetails should return match details from repository`() {
        val mockDetails = mapOf("id" to "1", "details" to "test")
        `when`(matchRepository.getMatchDetailed("uid")).thenReturn(mockDetails)

        val result = matchService.getMatchDetails("uid")

        assertEquals(mockDetails, result)
        verify(matchRepository).getMatchDetailed("uid")
    }
}