package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.Song
import main.kotlin.com.supplier.championleague.model.toMap

@ApplicationScoped
class SongRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val songCollection = db.collection("songs")

    fun getSongs(limit: Int?=25, offset: Int?=1): ArrayList<Map<String, Any>>? {
        
        val query = songCollection.limit(limit ?:50) // .offset(offset ?:1)
        val songSnapshot = query.get().get()
        val songs: List<DocumentSnapshot> = songSnapshot.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        songs.forEach{
            println("song: ${it.reference.id} => ${it.data}")
            val data = it.data
            if (data != null) {
                val song = Song(
                    id = it.reference.id,
                    song_file = data["song_file"] as? String,
                    name = data["name"] as? String,
                    duration = data["duration"] as? Int
                )
                list.add(song.toMap())
            }
        }
        return if(songSnapshot.size()>0) list else null
    }

    /***
     * getSong 
     * Parameters: uid = Unique identifier
     * Return a simple Map object with the referenced Ids
     */
    fun getSong(uid: String): Map<String, Any>? {
        val songSnapshot = songCollection.document(uid).get().get()
        if (songSnapshot.exists()){
            val data = songSnapshot.data!!
            val song = Song(
                id = uid,
                song_file = data["song_file"] as? String,
                name = data["name"] as? String,
                duration = data["duration"] as? Int
            )
            return song.toMap()
        }        
        return null
    }

}
