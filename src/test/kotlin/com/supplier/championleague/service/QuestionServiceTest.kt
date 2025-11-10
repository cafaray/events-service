package com.supplier.championleague.service

import com.supplier.championleague.repositories.QuestionRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class QuestionServiceTest {

    private lateinit var questionRepository: QuestionRepository
    private lateinit var questionService: QuestionService

    @BeforeEach
    fun setUp() {
        questionRepository = mock(QuestionRepository::class.java)
        questionService = QuestionService(questionRepository)
    }

    @Test
    fun `getQuestions should return questions from repository`() {
        val mockQuestions = arrayListOf<Map<String, Any>>(mapOf("id" to "1"))
        `when`(questionRepository.getQuestions(25, 1)).thenReturn(mockQuestions)

        val result = questionService.getQuestions(25, 1)

        assertEquals(mockQuestions, result)
        verify(questionRepository).getQuestions(25, 1)
    }

    @Test
    fun `getQuestion should return question from repository`() {
        val mockQuestion = mapOf("id" to "1")
        `when`(questionRepository.getQuestion("uid")).thenReturn(mockQuestion)

        val result = questionService.getQuestion("uid")

        assertEquals(mockQuestion, result)
        verify(questionRepository).getQuestion("uid")
    }
}