package main.kotlin.com.supplier.championleague.service

import jakarta.enterprise.context.ApplicationScoped
import com.supplier.championleague.repositories.QuestionnaireRepository

@ApplicationScoped
class QuestionnaireService(val questionnaireRepository: QuestionnaireRepository) {

    fun getQuestionnaires(limit: Int? = 25, offset: Int? = 1): ArrayList<Map<String, Any>>? {
        return questionnaireRepository.getQuestionnaires(limit, offset)
    }

    fun getQuestionnaire(uid: String): Map<String, Any>? {
        return questionnaireRepository.getQuestionnaire(uid)
    }
}