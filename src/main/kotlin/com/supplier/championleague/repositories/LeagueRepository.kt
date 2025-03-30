package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.League
import main.kotlin.com.supplier.championleague.model.Stadium
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ApplicationScoped
class LeagueRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val leagueCollection = db.collection("leagues")

    @OptIn(ExperimentalUuidApi::class)
    fun createLeague(league: League): League {
        val leagueId = league.id ?: (Uuid.random() as String)
        val newLeague = league.copy(id = leagueId)
        leagueCollection.add(newLeague).get()
        return newLeague
    }

    fun getLeagues(): ArrayList<Map<String, Any>?>? {
        val leagueSnapshots = leagueCollection.get().get()
        val leagues: List<DocumentSnapshot> = leagueSnapshots.toList()
        val list: ArrayList<Map<String, Any>?> = arrayListOf()
        leagues.forEach {
            // println(it.data)
            list.add(it.data)
        }
        return if (leagueSnapshots.size()>0) list else null
    }

    fun getLeague(uid: String): Map<String, Any>? {
        val leagueSnapshot = leagueCollection.document(uid).get().get()
        return if (leagueSnapshot.exists()) leagueSnapshot.data else null
    }

}