package com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import com.supplier.championleague.model.Question
import com.supplier.championleague.model.QuestionType
import com.supplier.championleague.model.QuestionClass

@ApplicationScoped
class QuestionRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val questionCollection = db.collection("questions")

    fun getQuestions(limit: Int? = 25, offset: Int? = 1): ArrayList<Map<String, Any>>? {
        val query = questionCollection.limit(limit ?: 50)
        val questionSnapshot = query.get().get()
        val questions: List<DocumentSnapshot> = questionSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        
        questions.forEach {
            println("question: ${it.reference.id} => ${it.data}")
            val data = it.data
            if (data != null) {
                val question = Question(
                    type = QuestionType.valueOf(data["type"] as String),
                    questionText = data["questionText"] as String,
                    questionClass = QuestionClass.valueOf(data["questionClass"] as String),
                    options = data["options"] as? List<String>,
                    correctAnswer = data["correctAnswer"]
                )
                list.add(mapOf(
                    "id" to it.reference.id,
                    "type" to question.type.toString(),
                    "questionText" to question.questionText,
                    "questionClass" to question.questionClass.toString(),
                    "options" to (question.options ?: emptyList<String>()),
                    "correctAnswer" to (question.correctAnswer ?: "")
                ))
            }
        }
        return if (questionSnapshot.size() > 0) list else null
    }

    fun getQuestion(uid: String): Map<String, Any>? {
        val questionSnapshot = questionCollection.document(uid).get().get()
        if (questionSnapshot.exists()) {
            val data = questionSnapshot.data!!
            val question = Question(
                type = QuestionType.valueOf(data["type"] as String),
                questionText = data["questionText"] as String,
                options = data["options"] as? List<String>,
                questionClass = QuestionClass.valueOf(data["questionClass"] as String),
                correctAnswer = data["correctAnswer"]
            )
            return mapOf(
                "id" to uid,
                "type" to question.type.toString(),
                "questionText" to question.questionText,
                "questionClass" to question.questionClass.toString(),
                "options" to (question.options ?: emptyList<String>()),
                "correctAnswer" to (question.correctAnswer ?: "")
            )
        }
        return null
    }
}