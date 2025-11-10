package com.supplier.championleague.service

import jakarta.enterprise.context.ApplicationScoped
import com.supplier.championleague.repositories.QuestionRepository

@ApplicationScoped
class QuestionService(val questionRepository: QuestionRepository) {

    fun getQuestions(limit: Int? = 25, offset: Int? = 1): ArrayList<Map<String, Any>>? {
        return questionRepository.getQuestions(limit, offset)
    }

    fun getQuestion(uid: String): Map<String, Any>? {
        return questionRepository.getQuestion(uid)
    }
}