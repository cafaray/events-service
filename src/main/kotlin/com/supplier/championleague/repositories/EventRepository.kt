package com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.supplier.championleague.model.Event
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import main.kotlin.com.supplier.championleague.model.League
import main.kotlin.com.supplier.championleague.model.TeamSimple
import main.kotlin.com.supplier.championleague.model.Venue
import main.kotlin.com.supplier.championleague.repositories.MatchRepository
import main.kotlin.com.supplier.championleague.repositories.VenuePositionRepository
import kotlin.collections.plus

@ApplicationScoped
class EventRepository (private val venuePositionRepository: VenuePositionRepository, private val matchRepository: MatchRepository){

    private val db: Firestore = FirestoreClient.getFirestore()
    private val eventCollection = db.collection("events")

    fun getAllEvents(): ArrayList<Map<String, Any>?>?{
        val snapshot = eventCollection.get().get()
        val events: List<DocumentSnapshot> = snapshot.toList()
        val list: ArrayList<Map<String, Any>?> = arrayListOf()
        events.forEach{
            // println(it.data)
            list.add(it.data)
        }
        return if(snapshot.size()>0) list else null
    }


    fun queryEvents(
        lat: Double?, long: Double?, date: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>?>?{
        var query = eventCollection.limit(limit ?: 50)
        var data: MutableMap<String, Any> = mutableMapOf()
        // You can append additional where conditions if needed
        println("Search params: lat: $lat, long: $long, date: $date, limit: $limit, offset: $offset")
        venuePositionRepository.findByLocation(lat ?: 0.0, long ?: 0.0)?.forEach {it ->
            println("venue: ${it.documentId}")
            println("Looking for a match with date: $date and venue: ${it.documentId}")
            matchRepository.getMatchesByQuery(venue=it.documentId, date=date, limit=limit, offset=offset, league = null, team = null)?.forEach {
                //query = query.whereEqualTo("matchId", it["id"])
                val venueData = (it["venue"] as DocumentReference).get().get().data
                val leagueData = (it["league"] as DocumentReference).get().get().data
                val teamLocal =  (it["team_local"] as DocumentReference).get().get() // .data
                val teamSimpleLocal = teamLocal.toObject(TeamSimple::class.java)
                val teamVisitor =  (it["team_visitor"] as DocumentReference).get().get() // .data
                val teamSimpleVisitor = teamVisitor.toObject(TeamSimple::class.java)
                data["date"] = it["date"] as String
                data["team_local"] = teamSimpleLocal as TeamSimple
                data["team_visitor"] = teamSimpleVisitor as TeamSimple
                data["venue"] = venueData as Map<String, Any>
                data["league"] = leagueData as Map<String, Any>
            }
            // query = query.whereEqualTo("venue", it.documentId)
        }
        val list: ArrayList<Map<String, Any>?> = arrayListOf()
        list.add(data)
        return list.ifEmpty { null }
    }
}