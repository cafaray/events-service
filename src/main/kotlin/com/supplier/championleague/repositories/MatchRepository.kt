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
    private val matchCollection = db.collection("matches")

    fun getMatches(limit: Int?=25, offset: Int?=1): ArrayList<Map<String, Any>>? {
        
        val query = matchCollection.limit(limit ?:50) // .offset(offset ?:1)
        val matchSnapshot = query.get().get()
        val matches: List<DocumentSnapshot> = matchSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        matches.forEach{
            println("match: ${it.reference.id} => ${it.data}")
            val data = it.data
            if (data != null) {
                val match = Match(
                    id = it.reference.id,
                    team_id_1 = (data["team_id_1"] as? DocumentReference)?.id,
                    team_id_2 = (data["team_id_2"] as? DocumentReference)?.id
                )
                list.add(match.toMap())
            }
        }
        return if(matchSnapshot.size()>0) list else null
    }

    /***
     * getMatch 
     * Parameters: uid = Unique identifier
     * Return a simple Map object with the referenced Ids
     */
    fun getMatch(uid: String): Map<String, Any>? {
        val matchSnapshot = matchCollection.document(uid).get().get()
        if (matchSnapshot.exists()){
            val data = matchSnapshot.data!!
            val match = Match(
                id = matchSnapshot.id,
                team_id_1 = (data["team_id_1"] as? DocumentReference)?.id,
                team_id_2 = (data["team_id_2"] as? DocumentReference)?.id
            )
            return match.toMap()
        }        
        return null
    }
    
    fun getMatchDetailed(uid: String): Map<String, Any>? {
        val matchSnapshot = matchCollection.document(uid).get().get()
        val matchData = matchSnapshot.data ?: return null     
        if (matchSnapshot.exists()){
            val response: MutableMap<String, Any> = mutableMapOf()            
            
            val teamId1 =  (matchData["team_id_1"] as DocumentReference).get().get() // .data
            val teamId1Simple = teamId1.toObject(TeamSimple::class.java)
            teamId1Simple?.id = teamId1.reference.id

            val teamId2 =  (matchData["team_id_2"] as DocumentReference).get().get() // .data            
            val teamId2Simple = teamId2.toObject(TeamSimple::class.java)
            teamId2Simple?.id = teamId2.reference.id
            
            response["id"] = uid
            response["team_id_1"] = teamId1Simple as TeamSimple
            response["team_id_2"] = teamId2Simple as TeamSimple
                        
            return response
        }        
        return null
    }    

    /***
     * getMatchesByQuery
     * params: team_id, limit, offset
     * Returns a List of detailed object matches where the team_id is present
     */
    fun getMatchesByQuery(team_id: String?, limit: Int?, offset: Int?): ArrayList<Map<String, Any>>? {
        var query = matchCollection.limit(limit ?: 50).offset(offset ?: 1)
        query.select("team_id_1", "team_id_2")
        // You can append additional where conditions if needed
        query.whereEqualTo("team_id_1", team_id)
        query.whereEqualTo("team_id_2", team_id)
        val matchSnapshot = query.get().get()
        val matches: List<DocumentSnapshot> = matchSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        val data: MutableMap<String, Any> = mutableMapOf()
        matches.forEach{
            // println(it.data)
            //list.add(it.data!!)
            
            val teamId1 =  (it["team_id_1"] as DocumentReference).get().get() // .data
            val teamId1Simple = teamId1.toObject(TeamSimple::class.java)
            teamId1Simple?.id = teamId1.reference.id

            val teamId2 =  (it["team_id_2"] as DocumentReference).get().get() // .data            
            val teamId2Simple = teamId2.toObject(TeamSimple::class.java)
            teamId2Simple?.id = teamId2.reference.id
            
            data["team_id_1"] = teamId1Simple as TeamSimple
            data["team_id_2"] = teamId2Simple as TeamSimple

            data["id"] = it.reference.id as String
            list.add(data)
        }
        return if(matchSnapshot.size()>0) list else null
    }

}
