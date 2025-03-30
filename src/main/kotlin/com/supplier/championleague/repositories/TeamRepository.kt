package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.Team
import main.kotlin.com.supplier.championleague.model.TeamSimple
import main.kotlin.com.supplier.championleague.model.toMap
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ApplicationScoped
class TeamRepository {
    private val db: Firestore = FirestoreClient.getFirestore()
    private val teamCollection = db.collection("teams")

    @OptIn(ExperimentalUuidApi::class)
    fun createTeam(team: Team): Team {
        val teamId = team.id ?: (Uuid.random() as String)
        val newTeam = team.copy(id = teamId)
        teamCollection.add(newTeam).get()
        return newTeam
    }

    fun getTeams(): ArrayList<Map<String, Any>?>? {
        val teamSnapshots = teamCollection.get().get()
        val teams: List<DocumentSnapshot> = teamSnapshots.toList()
        val list: ArrayList<Map<String, Any>?> = arrayListOf()
        teams.forEach {
            val teamSimple = it.toObject(TeamSimple::class.java)
            teamSimple?.id = it.reference.id
            if(teamSimple!=null){
                list.add(teamSimple.toMap())
            }
            //list.add(it.data)
        }
        return if (teamSnapshots.size()>0) list else null
    }

    fun getTeam(uid: String): Map<String, Any>? {
        val teamSnapshot = teamCollection.document(uid).get().get()
        if (teamSnapshot.exists()){
            val teamSimple = teamSnapshot.toObject(TeamSimple::class.java)
            teamSimple?.id = teamSnapshot.id
            return teamSimple?.toMap()
        }
        return null
    }

}