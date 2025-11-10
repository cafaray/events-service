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
import main.kotlin.com.supplier.championleague.repositories.VenueSearchPosRepository
import main.kotlin.com.supplier.championleague.repositories.EventMatchRepository
import main.kotlin.com.supplier.championleague.repositories.EventSongRepository
import kotlin.collections.plus

@ApplicationScoped
class EventRepository (
    private val venueSearchPosRepository: VenueSearchPosRepository, 
    private val eventMatchRepository: EventMatchRepository,
    private val eventSongRepository: EventSongRepository,
    private val eventQueryRepository: EventQueryRepository,){

    private val db: Firestore = FirestoreClient.getFirestore()
    
    private val ROW_LIMIT=10
    private val ROW_OFFSET=1
    private val SEARCHPOS_RADIO_DISTANCE=1000

    fun getEventById(
        type: String?,
        event_id: String?
    ): ArrayList<Map<String, Any>?>? {
        println("Starting getEventById with Search params:")
        println("* type: $type\n* event_id: $event_id")
        val data: ArrayList<MutableMap<String, Any>> = arrayListOf()
        if (EventType.MATCH.toString().equals(type) || type == null) {
            println("Querying by type: $type")
            var match = eventMatchRepository.getEventMatch(event_id ?: "")
            if (match != null) {
                data.add(mutableMapOf(
                    "id" to match["id"] as String,
                    "type" to EventType.MATCH.toString(),
                    "name" to match["name"] as String,
                    "date" to match["date"] as String,
                    "description" to (match["description"] as? String ?: ""),
                    "status" to match["status"] as String,
                    "details" to (match["events"] ?: emptyList<Any>())
                ))
            }
        } 
        if (EventType.SONG.toString() == type || type == null){
            println("Querying by type: $type")
            var song = eventSongRepository.getEventSong(event_id ?: "")
            if (song != null) {
                data.add(mutableMapOf(
                    "id" to song["id"] as String,
                    "type" to EventType.SONG.toString(),
                    "name" to song["name"] as String,
                    "date" to song["date"] as String,
                    "description" to (song["description"] as? String ?: ""),
                    "status" to song["status"] as String,
                    "song_id" to (song["song_id"] as? String ?: ""),
                    "team_id" to (song["team_id"] as? String ?: ""),
                    "start_at" to (song["start_at"] as? Long ?: 0L),
                    "start_in" to (song["start_in"] as? Long ?: 0L),
                    "details" to (song["details"] ?: emptyList<Any>())
                ))
            }            
        }
        return if(data.size > 0) ArrayList(data.map { it }) else null
    }

    fun getAllEvents(type: String?, name: String?, date: String?, status: String?, 
                    lat: Double?, long: Double?
    ): ArrayList<Map<String, Any>?>?{
        println("Starting queryEvents with Search params:")
        println("* type: $type\n* name: $name\n* date: $date\n* status: $status\n* lat: $lat\n* long: $long")
    
        // In case of longitude and latitude, the system assumes an EventMatch type.
        // Then we need to find the nearest venue first
        if ((lat != null && long != null) && (lat != 0.0 && long != 0.0)) {
            println("Querying by position: lat $lat , long: $long")
            return getEventMatchsByPosition(long, lat, date, status, name)
        } 
        // Otherwise, collect at least first ten events per type and summarize them into the `data` list        
        // If no longitude and latitude, then we look for an Event type, based on the type parameter or all types if type is null
        // `data` will collect all the events to reply
        var data: ArrayList<MutableMap<String, Any>> = arrayListOf()
        
        if (EventType.MATCH.toString().equals(type) || type == null) {
            println("Querying by type: $type")
            var matches =eventMatchRepository.getEventMatches(name, date, status, ROW_LIMIT, ROW_OFFSET)
            // "date", "name", "league", "venue", "match", "status", "time_starting", "time_ending", "team_id_local", "events"
            matches?.forEach{                
                data.add(mutableMapOf(
                    "id" to it["id"] as String,
                    "type" to EventType.MATCH.toString(),
                    "name" to it["name"] as String,
                    "date" to it["date"] as String,
                    "description" to (it["description"] as? String ?: ""),
                    "status" to it["status"] as String,
                    "details" to (it["events"] ?: emptyList<Any>())
                ))
            }
        } 
        if (EventType.SONG.toString() == type || type == null){
            println("Querying by type: $type")
            var songs = eventSongRepository.getEventSongs(name, date, status, ROW_LIMIT, ROW_OFFSET)
            songs?.forEach{
                data.add(mutableMapOf(
                    "id" to it["id"] as String,
                    "type" to EventType.SONG.toString(),
                    "name" to it["name"] as String,
                    "date" to it["date"] as String,
                    "description" to (it["description"] as? String ?: ""),
                    "status" to it["status"] as String,
                    "details" to (it["details"] ?: emptyList<Any>())
                ))
            }            
        }
        if (EventType.QUERY.toString() == type || type == null) {
            println("Querying by type: $type")
            var queries = eventQueryRepository.getEventQueries(name, date, status, ROW_LIMIT, ROW_OFFSET)
            queries?.forEach{
                data.add(mutableMapOf(
                    "id" to it["id"] as String,
                    "type" to EventType.QUERY.toString(),
                    "name" to it["name"] as String,
                    "date" to it["date"] as String,
                    "description" to (it["description"] as? String ?: ""),
                    "status" to it["status"] as String,
                    "details" to (it["events"] ?: emptyList<Any>())
                ))
            }
        }
        /* 
        if(EventType.CAST.toString() == type || type == null){
            println("Querying by type: $type")
            return getEventCast(date ?: "", status ?: "", name ?: "")
        }
        */
        
        return if(data.size > 0) ArrayList(data.map { it }) else data.let { null }
    }

    fun getEventMatchsByPosition(long: Double, lat: Double, date: String?, status: String?, name: String?): ArrayList<Map<String, Any>?>? {
        // Find nearest venue by lat and long
        // Then find matchs for that venue
        println("** Querying for matches by position:\n->  lat = ${lat}\n->  long: ${long}")
        println("** Filtering by:\n-> date: $date,\n-> status: $status,\n-> name: $name")
        var data: MutableMap<String, Any> = mutableMapOf()        
        var events: ArrayList<Map<String, Any>> = arrayListOf()        
        val list: ArrayList<Map<String, Any>?> = arrayListOf()
        venueSearchPosRepository.getNearby(lat, long, maxDistance=SEARCHPOS_RADIO_DISTANCE).forEach {it ->
            println("venue: ${it.venue_id}")
            println("Looking for a match with date: $date and venue: ${it.venue_id}")
            if (it.venue_id == null) {
                println("No venue id found, skipping to next venue...")
                return@forEach
            }
            eventMatchRepository.getEventMatchByQuery(date=date, team_id_local=null, venue_id=it.venue_id, name=name, status=status, limit=25, offset=1)?.forEach {e ->
                data["date"] = e["date"] as String
                data["type"] = EventType.MATCH.toString()
                data["name"] = e["name"] as String
                data["description"] = e["description"] as String? ?: ""
                data["status"] = e["status"] as String
                data["team_id_local"] = e["team_id_local"] as String
                data["time_starting"] = e["time_starting"] as String
                data["time_ending"] = e["time_ending"] as String                
                data["match_id"] = e["match_id"] as String
                data["venue_id"] = e["venue_id"] as String
                data["league_id"] = e["league_id"] as String
                var associatedEvents: ArrayList<Map<String, Any>>?
                associatedEvents = e["events"] as? ArrayList<Map<String, Any>>
                associatedEvents?.forEach { ev ->
                    var event: MutableMap<String, Any> = mutableMapOf()
                    event["id"] = ev["event_id"] as String
                    event["type"] = ev["event_type"] as String
                    events.add(event)
                }
                data["events"] = events
                list.add(data)
            }            
            
        }
        return list.ifEmpty { null }
    }
}