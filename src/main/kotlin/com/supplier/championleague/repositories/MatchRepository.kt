package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.Match
import main.kotlin.com.supplier.championleague.model.TeamSimple
import main.kotlin.com.supplier.championleague.model.toMap

@ApplicationScoped
class MatchRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val matchCollection = db.collection("matchs")

    fun getMatches(): ArrayList<Map<String, Any>>? {
        val matchSnapshot = matchCollection.get().get()
        val matches: List<DocumentSnapshot> = matchSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        matches.forEach{
            val matchSimple = it.toObject(Match::class.java)
            matchSimple?.id = it.reference.id
            //list.add(it.data!!)
            if (matchSimple != null) {
                list.add(matchSimple.toMap())
            }
        }
        return if(matchSnapshot.size()>0) list else null
    }

    fun getMatch(uid: String): Map<String, Any>? {
        val matchSnapshot = matchCollection.document(uid).get().get()
        if (matchSnapshot.exists()){
            val matchSimple = matchSnapshot.toObject(Match::class.java)
            //val matchSimple = matchSnapshot.toMap()
            matchSimple?.id = matchSnapshot.id
            return matchSimple?.toMap()
        }
        return null
    }

    fun getMatchesByQuery(
        date: String?, team: String?, venue: String?, league: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>>? {
        var query = matchCollection.limit(limit ?: 50)
        query.select("date", "team_local", "team_visitor", "venue", "league")
        // You can append additional where conditions if needed
        query.whereEqualTo("date", date)
        query.whereEqualTo("venue", venue)

        val snapshot = query.get().get()
        val matches: List<DocumentSnapshot> = snapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        val data: MutableMap<String, Any> = mutableMapOf()
        matches.forEach{
            // println(it.data)
            //list.add(it.data!!)
            val venueData = (it["venue"] as DocumentReference).get().get().data
            val leagueData = (it["league"] as DocumentReference).get().get().data
            val teamLocal =  (it["team_local"] as DocumentReference).get().get() // .data
            val teamSimpleLocal = teamLocal.toObject(TeamSimple::class.java)
            teamSimpleLocal?.id = teamLocal.reference.id
            val teamVisitor =  (it["team_visitor"] as DocumentReference).get().get() // .data
            val teamSimpleVisitor = teamVisitor.toObject(TeamSimple::class.java)
            teamSimpleVisitor?.id = teamVisitor.reference.id
            data["date"] = it["date"] as String
            data["name"] = it["name"] as String
            data["status"] = it["status"] as String
            data["team_local"] = teamSimpleLocal as TeamSimple
            data["team_visitor"] = teamSimpleVisitor as TeamSimple
            data["venue"] = venueData as Map<String, Any>
            data["league"] = leagueData as Map<String, Any>
            data["summary"] = it["summary"] as Map<String, Any>

            data["id"] = it.reference.id as String
            list.add(data)
        }
        return if(snapshot.size()>0) list else null
    }

    // use `suspend fun ....` when you need a subroutine
    fun getMatchDetails(matchId: String): Map<String, Any>? {
        val matchDoc = matchCollection.document(matchId).get().get()
        if (!matchDoc.exists()) return null
        val matchData = matchDoc.data ?: return null
        val venueData = (matchData["venue"] as DocumentReference).get().get().data
        val leagueData = (matchData["league"] as DocumentReference).get().get().data
        val teamLocal =  (matchData["team_local"] as DocumentReference).get().get() // .data
        val teamSimpleLocal = teamLocal.toObject(TeamSimple::class.java)
        val teamVisitor =  (matchData["team_visitor"] as DocumentReference).get().get() // .data
        val teamSimpleVisitor = teamVisitor.toObject(TeamSimple::class.java)
        return if (venueData!=null && leagueData!=null && teamSimpleLocal!=null && teamSimpleVisitor!=null) {
            (matchData
                + ("venue" to venueData)
                + ("league" to leagueData)
                + ("team_local" to teamSimpleLocal)
                + ("team_visitor" to teamSimpleVisitor)
            )
        }else {
            return matchData
        }
    }
}
