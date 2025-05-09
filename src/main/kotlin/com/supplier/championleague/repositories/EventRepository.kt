package com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.supplier.championleague.model.Event
import com.supplier.championleague.model.EventType
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
        println("Starting queryEvents with Search params: lat: $lat, long: $long, date: $date, limit: $limit, offset: $offset")
        var query = eventCollection.limit(limit ?: 50)
        var data: MutableMap<String, Any> = mutableMapOf()
        // You can append additional where conditions if needed
        println("Search params: lat: $lat, long: $long, date: $date, limit: $limit, offset: $offset")
        venuePositionRepository.findByLocation(lat ?: 0.0, long ?: 0.0)?.forEach {it ->
            println("venue: ${it.documentId}")
            println("Looking for a match with date: $date and venue: ${it.documentId}")
            var details: MutableMap<String, Any> = mutableMapOf()
            matchRepository.getMatchesByQuery(venue=it.documentId, date=date, limit=limit, offset=offset, league = null, team = null)?.forEach {
                //query = query.whereEqualTo("matchId", it["id"])

                data["date"] = it["date"] as String
                data["type"] = EventType.MATCH.toString() as String
                data["name"] = it["name"] as String
                    details["id"] = it["id"] as String
                    details["team_local"] = it["team_local"] as TeamSimple
                    details["team_visitor"] = it["team_visitor"] as TeamSimple
                    details["venue"] = it["venue"] as Map<String, Any>
                    details["league"] = it["league"] as Map<String, Any>
                data["details"] = details as Map<String, Any>
            }
            // query = query.whereEqualTo("venue", it.documentId)
        }
        val list: ArrayList<Map<String, Any>?> = arrayListOf()
        list.add(data)
        return list.ifEmpty { null }
    }
}