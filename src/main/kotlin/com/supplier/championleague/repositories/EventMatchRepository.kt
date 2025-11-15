package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.EventMatch
import main.kotlin.com.supplier.championleague.model.TeamSimple
import main.kotlin.com.supplier.championleague.model.toMap
import main.kotlin.com.supplier.championleague.model.Attendee

@ApplicationScoped
class EventMatchRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val eventMatchCollection = db.collection("event_match")
    private val COLLECTION_ATTENDEES: String = "attendees"
    private val COLLECTION_USERS: String = "users"

    private fun createDetailsMap(geoposition: Map<String, Any>): Map<String, Any> {
        return mapOf(
            "accuracy" to (geoposition["accuracy"] ?: 0),
            "lat" to (geoposition["lat"] ?: 0.0),
            "lng" to (geoposition["lng"] ?: 0.0),
            "timestamp" to System.currentTimeMillis()
        )
    }

    fun getEventMatches(): ArrayList<Map<String, Any>>? {
        val matchSnapshot = eventMatchCollection.get().get()
        val matches: List<DocumentSnapshot> = matchSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        matches.forEach{
            val match = it.toObject(EventMatch::class.java)
            match?.id = it.reference.id
            //list.add(it.data!!)
            if (match != null) {
                list.add(match.toMap())
            }
        }
        return if(matchSnapshot.size()>0) list else null
    }

    fun getEventMatches(name: String?, date: String?, status: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>>? {
        println("Starting queryEventMatches with search params:")
        println("* name: $name\n* date: $date\n* status: $status")
        val eventMatches = getEventMatchByQuery(date=date, name=name, status=status, team_id_local=null, venue_id=null, limit=limit, offset=offset)
        return if (eventMatches?.size ?: 0 > 0) eventMatches else null
    }

    fun getEventMatch(uid: String): Map<String, Any>? {
        val matchSnapshot = eventMatchCollection.document(uid).get().get()
        if (matchSnapshot.exists()){
            val matchSimple = matchSnapshot.toObject(EventMatch::class.java)
            matchSimple?.id = matchSnapshot.id
            return matchSimple?.toMap()
        }
        return null
    }

    fun getEventMatchByQuery(
        date: String?, team_id_local: String?, venue_id: String?, name: String?, status: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>>? {
        var query = eventMatchCollection.limit(limit ?: 50)
        // query.select("date", "name", "league", "venue", "match", "status", "time_starting", "time_ending", "team_id_local", "events")
        // You can append additional where conditions if needed. 
        // Remember that Firestore does not support OR queries directly.
        if (date != null) query.whereEqualTo("date", date)
        if (team_id_local != null) query.whereEqualTo("team_id_local", team_id_local)
        if (venue_id != null) query.whereEqualTo("venue_id", venue_id)
        if (name != null) query.whereEqualTo("name", name)
        if (status != null) query.whereEqualTo("status", status)

        val snapshot = query.get().get()
        val matches: List<DocumentSnapshot> = snapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        val data: MutableMap<String, Any> = mutableMapOf()
        matches.forEach{
            // println(it.data)
            //list.add(it.data!!)
            // TODO: review if the following is needed
            // val venueData = (it["venue"] as DocumentReference).get().get().data
            // val leagueData = (it["league"] as DocumentReference).get().get().data
            // val matchData = (it["match"] as DocumentReference).get().get().data
            // println("venueData: ${venueData}")  <-- DEBUG: All the venue data structure 
            // println("venueData: ${(it["venue"] as DocumentReference).id}")  <-- DEBUG: Venue ID only
            data["venue_id"] = (it["venue"] as DocumentReference).id
            data["league_id"] = (it["league"] as DocumentReference).id
            data["match_id"] = (it["match"] as DocumentReference).id
            data["date"] = it["date"] as String
            data["name"] = it["name"] as String
            data["description"] = it["description"] as String
            data["team_id_local"] = it["team_id_local"] as String            
            data["status"] = it["status"] as String
            data["time_starting"] = it["time_starting"] as String
            data["time_ending"] = it["time_ending"] as String
            data["events"] = it["events"] as ArrayList<Map<String, Any>>
            data["id"] = it.reference.id as String
            println("match found: ${data}")
            list.add(data)
        }
        return if(snapshot.size()>0) list else null
    }

    fun saveEventMatchAttendee(
        eventMatchId: String,
        userId: String,
        geoposition: Map<String, Any>
    ): String? {
        val attendeeData = mapOf(
            "id" to db.collection(COLLECTION_USERS).document(userId),
            "confirmedAt" to System.currentTimeMillis(),
            "recorded" to false,
            "details" to arrayListOf(createDetailsMap(geoposition))
        )
        
        eventMatchCollection
            .document(eventMatchId)
            .collection(COLLECTION_ATTENDEES)
            .document(userId)
            .set(attendeeData)
            .get()
        
        return userId
    }

    fun updateEventMatchAttendeeDetails(
        eventMatchId: String,
        userId: String,
        geoposition: Map<String, Any>
    ): String? {
        val attendeeRef = eventMatchCollection
            .document(eventMatchId)
            .collection(COLLECTION_ATTENDEES)
            .document(userId)
        
        val attendeeDoc = attendeeRef.get().get()
        if (!attendeeDoc.exists()) {
            return null
        }
        
        val currentDetails = attendeeDoc.get("details") as? ArrayList<Map<String, Any>> ?: arrayListOf()
        currentDetails.add(createDetailsMap(geoposition))
        
        attendeeRef.update("details", currentDetails).get()
        
        return userId
    }
}
