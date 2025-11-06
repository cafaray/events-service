package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.supplier.championleague.model.EventType
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.EventSong
import main.kotlin.com.supplier.championleague.model.toMap
import java.time.LocalDate

@ApplicationScoped
class EventSongRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val eventSongCollection = db.collection("event_song")

    fun getEventSongs(): ArrayList<Map<String, Any>>? {
        val songSnapshot = eventSongCollection.get().get()
        val matches: List<DocumentSnapshot> = songSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        matches.forEach{
            println("eventSong: ${it.reference.id} => ${it.data}")
            val data = it.data
            if (data != null) {
                val eventSong = EventSong(
                    id = it.id,
                    name = it["name"] as? String,
                    status = it["status"] as? String,
                    date = it["date"] as? String,
                    type = EventType.SONG.toString()
                )
                list.add(eventSong.toMap())
            }
        }
        return if(songSnapshot.size()>0) list else null        
    }

    fun getEventSongs(name: String?, date: String?, status: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>>? {
        println("Starting queryEventSongs with search params:")
        println("* name: $name\n* date: $date\n* status: $status")
        val eventSongs = getEventSongByQuery(date=date, name=name, status=status, limit=limit, offset=offset)
        return if (eventSongs != null) eventSongs else null
    }

    fun getEventSong(uid: String): Map<String, Any>? {
        val songSnapshot = eventSongCollection.document(uid).get().get()
        if (songSnapshot.exists()){
            println("eventSong: ${songSnapshot.reference.id} => ${songSnapshot.data}")
            val data = songSnapshot.data
            if (data != null) {
                val startAt = (songSnapshot["start_at"] as? Long ?: 0L)                                
                val start_in_ms = getSongTimeToPlay(startAt)
                if (start_in_ms < 0){
                    println("*   ERROR: start_in_ms is negative for eventSong id: ${songSnapshot.id}")
                    // return null
                }
                println("start_in_ms: $start_in_ms")
                val eventSong = EventSong(
                    id = songSnapshot.id,
                    name = songSnapshot["name"] as? String,
                    status = songSnapshot["status"] as? String,
                    date = songSnapshot["date"] as? String,
                    song_id = songSnapshot["song_id"] as? String,
                    team_id = songSnapshot["team_id"] as? String,
                    type = EventType.SONG.toString(),
                    start_at = songSnapshot["start_at"] as? Long,
                    start_in = start_in_ms 
                )
                return eventSong.toMap()
            }
        }
        return null
    }

    fun getEventSongByQuery(
        date: String?, name: String?, status: String?, limit: Int?, offset: Int?
    ): ArrayList<Map<String, Any>>? {
        var query = eventSongCollection.limit(limit ?: 50)
        query.select("date", "name", "status", "song_id", "time_starting", "time_ending", "team_id")
        // You can append additional where conditions if needed. 
        // Remember that Firestore does not support OR queries directly.
        if (date != null) query.whereEqualTo("date", date)                
        if (name != null) query.whereEqualTo("name", name)
        if (status != null) query.whereEqualTo("status", status)

        val snapshot = query.get().get()
        val eventSongs: List<DocumentSnapshot> = snapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()        
        eventSongs.forEach{
            val startAt = (it["start_at"] as? Long ?: 0L)                                
            val start_in_ms = getSongTimeToPlay(startAt)
            if (start_in_ms < 0){
                println("*   ERROR: start_in_ms is negative for eventSong id: ${it.id}")
                // return null
            }
            println("start_in_ms: $start_in_ms")

            val eventSong = EventSong(
                id = it.reference.id,
                name = it["name"] as? String,
                status = it["status"] as? String,
                date = it["date"] as? String,
                song_id = it["song_id"] as? String,
                team_id = it["team_id"] as? String,
                type = EventType.SONG.toString(),
                start_at = it["start_at_ms"] as? Long,
                start_in = start_in_ms
            )            
            list.add(eventSong.toMap())
        }
        return if(snapshot.size()>0) list else null
    }

    fun getSongTimeToPlay(startAt: Long): Long {
        // evaluate time fields, when the song starts playing
        val currentInstant: java.time.Instant = java.time.Instant.now()
        val currentTimestamp: Long = currentInstant.toEpochMilli()        
        if(startAt <= currentTimestamp){
            println("*   ERROR: start_at ${startAt} is less than current time ${currentTimestamp}")
            return -1
        }
        return startAt - currentTimestamp        
    }

}
