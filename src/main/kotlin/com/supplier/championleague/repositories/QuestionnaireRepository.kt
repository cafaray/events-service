package com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import com.supplier.championleague.model.Questionnaire
import com.supplier.championleague.model.QuestionnaireType
import com.supplier.championleague.model.Question
import com.supplier.championleague.model.QuestionType
import com.supplier.championleague.model.QuestionClass

@ApplicationScoped
class QuestionnaireRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val questionnaireCollection = db.collection("questionnaires")

    fun getQuestionnaires(limit: Int? = 25, offset: Int? = 1): ArrayList<Map<String, Any>>? {
        val query = questionnaireCollection.limit(limit ?: 50)
        val questionnaireSnapshot = query.get().get()
        val questionnaires: List<DocumentSnapshot> = questionnaireSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        
        questionnaires.forEach {
            println("questionnaire: ${it.reference.id} => ${it.data}")
            val data = it.data
            if (data != null) {
                val questions = (data["questions"] as? List<Map<String, Any>>)?.map { questionData ->
                    Question(
                        type = QuestionType.valueOf(questionData["type"] as String),
                        questionText = questionData["questionText"] as String,                        
                        questionClass = QuestionClass.valueOf(data["questionClass"] as String),
                        options = questionData["options"] as? List<String>,
                        correctAnswer = questionData["correctAnswer"]
                    )
                } ?: emptyList()

                val questionnaire = Questionnaire(
                    name = data["name"] as String,
                    description = data["description"] as String,
                    type = QuestionnaireType.valueOf(data["type"] as String),
                    questions = questions
                )
                
                list.add(mapOf(
                    "id" to it.reference.id,
                    "name" to questionnaire.name,
                    "description" to questionnaire.description,
                    "type" to questionnaire.type.toString(),
                    "questions" to questionnaire.questions.map { question ->
                        mapOf(
                            "type" to question.type.toString(),
                            "questionText" to question.questionText,
                            "questionClass" to question.questionClass,
                            "options" to question.options,
                            "correctAnswer" to question.correctAnswer
                        )
                    }
                ))
            }
        }
        return if (questionnaireSnapshot.size() > 0) list else null
    }

    fun getQuestionnaire(uid: String): Map<String, Any>? {
        val questionnaireSnapshot = questionnaireCollection.document(uid).get().get()
        if (questionnaireSnapshot.exists()) {
            val data = questionnaireSnapshot.data!!
            val questions = (data["questions"] as? List<Map<String, Any>>)?.map { questionData ->
                Question(
                    type = QuestionType.valueOf(questionData["type"] as String),
                    questionText = questionData["questionText"] as String,
                    questionClass = QuestionClass.valueOf(data["questionClass"] as String),
                    options = questionData["options"] as? List<String>,
                    correctAnswer = questionData["correctAnswer"]
                )
            } ?: emptyList()

            val questionnaire = Questionnaire(
                name = data["name"] as String,
                description = data["description"] as String,
                type = QuestionnaireType.valueOf(data["type"] as String),
                questions = questions
            )
            
            return mapOf(
                "id" to uid,
                "name" to questionnaire.name,
                "description" to questionnaire.description,
                "type" to questionnaire.type.toString(),
                "questions" to questionnaire.questions.map { question ->
                    mapOf(
                        "type" to question.type.toString(),
                        "questionText" to question.questionText,
                        "questionClass" to question.questionClass,
                        "options" to question.options,
                        "correctAnswer" to question.correctAnswer
                    )
                }
            )
        }
        return null
    }
}