package main.kotlin.com.supplier.championleague.service

import main.kotlin.com.supplier.championleague.model.User
import main.kotlin.com.supplier.championleague.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        userService = UserService(userRepository)
    }

    @Test
    fun `postUser should create user and return user data`() {
        val mockUser = mapOf("id" to "uid", "name" to "Test User", "email" to "test@example.com")
        `when`(userRepository.getUser("uid")).thenReturn(mockUser)

        val result = userService.postUser("uid", "Test User", "test@example.com")

        assertEquals(mockUser, result)
        verify(userRepository).createUser(any(User::class.java))
        verify(userRepository).getUser("uid")
    }

    @Test
    fun `getUsers should return users from repository`() {
        val mockUsers = arrayListOf<Map<String, Any>>(mapOf("id" to "1"))
        `when`(userRepository.getUsers()).thenReturn(mockUsers)

        val result = userService.getUsers()

        assertEquals(mockUsers, result)
        verify(userRepository).getUsers()
    }

    @Test
    fun `getUser should return user from repository`() {
        val mockUser = mapOf("id" to "uid")
        `when`(userRepository.getUser("uid")).thenReturn(mockUser)

        val result = userService.getUser("uid")

        assertEquals(mockUser, result)
        verify(userRepository).getUser("uid")
    }
}