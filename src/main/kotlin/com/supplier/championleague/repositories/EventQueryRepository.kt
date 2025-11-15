package com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import com.supplier.championleague.model.EventQuery
import com.supplier.championleague.model.EventQueryDetails
import com.supplier.championleague.model.EventStatus
import com.supplier.championleague.model.Questionnaire
import com.supplier.championleague.model.QuestionnaireType
import com.supplier.championleague.model.Question
import com.supplier.championleague.model.QuestionType
import com.supplier.championleague.model.QuestionClass
import javax.print.Doc
import main.kotlin.com.supplier.championleague.model.Attendee

@ApplicationScoped
class EventQueryRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val eventQueryCollection = db.collection("event_query")
    private val COLLECTION_ATTENDEES: String = "attendees"
    private val COLLECTION_USERS: String = "users"


    fun getEventQueries(name: String?, date: String?, status: String?, limit: Int? = 25, offset: Int? = 1): ArrayList<Map<String, Any>>? {
        println("Starting queryEventQueries with search params:")
        println("* name: $name\n* date: $date\n* status: $status")

        val query = eventQueryCollection.limit(limit ?: 50)
        query.whereGreaterThanOrEqualTo("name", name ?: "")
        if (date != null) {
            query.whereEqualTo("date", date)
        }
        if (status != null) {
            query.whereEqualTo("status", status)
        }
        val eventQuerySnapshot = query.get().get()
        val eventQueries: List<DocumentSnapshot> = eventQuerySnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        
        eventQueries.forEach {
            println("eventQuery: ${it.reference.id} => ${it.data}")
            val data = it.data
            if (data != null) {
                val eventQuery = EventQuery(
                    name = data["name"] as String,
                    description = data["description"] as? String,
                    date = data["date"] as String,
                    type = data["type"] as? String ?: "QUERY",
                    status = EventStatus.valueOf(data["status"] as String),
                    questionaireId = data["questionaire_id"] as String,
                    timeToAnswer = (data["time_to_answer"] as Long).toInt()
                )
                
                list.add(mapOf(
                    "id" to it.reference.id,
                    "name" to eventQuery.name,
                    "description" to (eventQuery.description ?: ""),
                    "date" to eventQuery.date,
                    "type" to eventQuery.type,
                    "status" to eventQuery.status.toString(),
                    "questionaire_id" to eventQuery.questionaireId,
                    "time_to_answer" to eventQuery.timeToAnswer
                ))
            }
        }
        return if (eventQuerySnapshot.size() > 0) list else null
    }

    fun getEventQuery(uid: String): Map<String, Any>? {
        val eventQuerySnapshot = eventQueryCollection.document(uid).get().get()
        if (eventQuerySnapshot.exists()) {
            val data = eventQuerySnapshot.data!!
            val eventQuery = EventQuery(
                name = data["name"] as String,
                description = data["description"] as? String,
                date = data["date"] as String,
                type = data["type"] as? String ?: "QUERY",
                status = EventStatus.valueOf(data["status"] as String),
                questionaireId = data["questionaire_id"] as String,
                timeToAnswer = (data["time_to_answer"] as Long).toInt()
            )
            
            return mapOf(
                "id" to uid,
                "name" to eventQuery.name,
                "description" to (eventQuery.description ?: ""),
                "date" to eventQuery.date,
                "type" to eventQuery.type,
                "status" to eventQuery.status.toString(),
                "questionaire_id" to eventQuery.questionaireId,
                "time_to_answer" to eventQuery.timeToAnswer
            )
        }
        return null
    }

    fun getEventQueryDetails(uid: String): Map<String, Any>? {
        val eventQuerySnapshot = eventQueryCollection.document(uid).get().get()
        if (eventQuerySnapshot.exists()) {
            val data = eventQuerySnapshot.data!!
            val questionaireId = data["questionaire_id"] as String
            
            // Fetch questionnaire from questionnaires collection
            val questionnaireDoc = db.collection("questionnaires").document(questionaireId).get().get()
            if (!questionnaireDoc.exists()) return null
            val questionnaireData = questionnaireDoc.data!!
            // print("\n*  questionnaireDoc.exists: ${questionnaireDoc.data!!}")
            //print("\n***  questionnaireData[questions]: ${questionnaireData["questions"]}")            
            val questions = (questionnaireData["questions"] as? List<Any>)?.map { questionDoc ->
                val questionData = (questionDoc as DocumentReference).get().get()
                
                Question(
                    type = QuestionType.valueOf(questionData["type"] as? String ?: "TEXT"),
                    questionText = questionData["questionText"] as? String ?: "",
                    questionClass = QuestionClass.valueOf(questionData["questionClass"] as? String ?: "TEAM"),
                    options = questionData["options"] as? List<String>,
                    correctAnswer = questionData["correctAnswer"]
                )
            } ?: emptyList()

            val questionnaire = Questionnaire(
                name = questionnaireData["name"] as? String ?: "",
                description = questionnaireData["description"] as? String ?: "",
                type = QuestionnaireType.valueOf(questionnaireData["type"] as? String ?: "TRIVIA"),
                questions = questions
            )

            val eventQueryDetails = EventQueryDetails(
                name = data["name"] as? String ?: "",
                description = data["description"] as? String,
                date = data["date"] as? String ?: "",
                type = data["type"] as? String ?: "QUERY",
                status = EventStatus.valueOf(data["status"] as? String ?: "UPCOMING"),
                questionaireId = questionaireId,
                timeToAnswer = (data["time_to_answer"] as? Long)?.toInt() ?: 0,
                questionnaire = questionnaire
            )
            
            return mapOf(
                "id" to uid,
                "name" to eventQueryDetails.name,
                "description" to (eventQueryDetails.description ?: ""),
                "date" to eventQueryDetails.date,
                "type" to eventQueryDetails.type,
                "status" to eventQueryDetails.status.toString(),
                "questionaire_id" to eventQueryDetails.questionaireId,
                "time_to_answer" to eventQueryDetails.timeToAnswer,
                "questionnaire" to mapOf(
                    "name" to questionnaire.name,
                    "description" to questionnaire.description,
                    "type" to questionnaire.type.toString(),
                    "questions" to questionnaire.questions.map { question ->
                        mapOf(
                            "type" to question.type.toString(),
                            "questionText" to question.questionText,
                            "questionClass" to question.questionClass.toString(),
                            "options" to (question.options ?: emptyList<String>()),
                            "correctAnswer" to (question.correctAnswer ?: "")
                        )
                    }
                )
            )
        }
        return null
    }

    fun saveEventQueryAttendee(
        eventQueryId: String,
        userId: String
    ): String? {
        val timestamp = System.currentTimeMillis()
        val recorded = false
        val attendeeData = mapOf(
            "id" to db.collection(COLLECTION_USERS).document(userId),
            "confirmedAt" to timestamp,
            "recorded" to recorded
        )
        
        val docRef = eventQueryCollection
            .document(eventQueryId)
            .collection(COLLECTION_ATTENDEES)
            .document(userId)
            .set(attendeeData)
            .get()
        
        return userId
    }

    fun saveEventQueryAnswers(
        eventQueryId: String,
        userId: String,
        answers: List<Map<String, Any>>
    ): String? {
        val attendeeDoc = eventQueryCollection
            .document(eventQueryId)
            .collection(COLLECTION_ATTENDEES)
            .document(userId)
        
        val updateData = mapOf(
            "details" to answers
        )
        
        attendeeDoc.update(updateData).get()
        return userId
    }
}