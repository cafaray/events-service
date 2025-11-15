package main.kotlin.com.supplier.championleague.service

import main.kotlin.com.supplier.championleague.repositories.SongRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class SongServiceTest {

    private lateinit var songRepository: SongRepository
    private lateinit var songService: SongService

    @BeforeEach
    fun setUp() {
        songRepository = mock(SongRepository::class.java)
        songService = SongService(songRepository)
    }

    @Test
    fun `getSongs should return songs from repository`() {
        val mockSongs = arrayListOf<Map<String, Any>>(mapOf("id" to "1"))
        `when`(songRepository.getSongs()).thenReturn(mockSongs)

        val result = songService.getSongs()

        assertEquals(mockSongs, result)
        verify(songRepository).getSongs()
    }

    @Test
    fun `getSong should return song from repository`() {
        val mockSong = mapOf("id" to "1")
        `when`(songRepository.getSong("uid")).thenReturn(mockSong)

        val result = songService.getSong("uid")

        assertEquals(mockSong, result)
        verify(songRepository).getSong("uid")
    }
}