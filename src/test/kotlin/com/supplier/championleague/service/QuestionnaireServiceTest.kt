package main.kotlin.com.supplier.championleague.service

import com.supplier.championleague.repositories.QuestionnaireRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach

class QuestionnaireServiceTest {

    private lateinit var questionnaireRepository: QuestionnaireRepository
    private lateinit var questionnaireService: QuestionnaireService

    @BeforeEach
    fun setUp() {
        questionnaireRepository = mock(QuestionnaireRepository::class.java)
        questionnaireService = QuestionnaireService(questionnaireRepository)
    }

    @Test
    fun `getQuestionnaires should return questionnaires from repository`() {
        val mockQuestionnaires = arrayListOf<Map<String, Any>>(mapOf("id" to "1"))
        `when`(questionnaireRepository.getQuestionnaires(25, 1)).thenReturn(mockQuestionnaires)

        val result = questionnaireService.getQuestionnaires(25, 1)

        assertEquals(mockQuestionnaires, result)
        verify(questionnaireRepository).getQuestionnaires(25, 1)
    }

    @Test
    fun `getQuestionnaire should return questionnaire from repository`() {
        val mockQuestionnaire = mapOf("id" to "1")
        `when`(questionnaireRepository.getQuestionnaire("uid")).thenReturn(mockQuestionnaire)

        val result = questionnaireService.getQuestionnaire("uid")

        assertEquals(mockQuestionnaire, result)
        verify(questionnaireRepository).getQuestionnaire("uid")
    }
}