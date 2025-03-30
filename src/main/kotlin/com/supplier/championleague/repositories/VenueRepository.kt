package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.Venue
import main.kotlin.com.supplier.championleague.model.Team
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ApplicationScoped
class VenueRepository {

    private val db: Firestore = FirestoreClient.getFirestore()
    private val venueCollection = db.collection("venues")

    @OptIn(ExperimentalUuidApi::class)
    fun createVenue(venue: Venue): Venue {
        val venueId = venue.id ?: (Uuid.random() as String)
        val newVenue = venue.copy(id = venueId)
        venueCollection.add(newVenue).get()
        return newVenue
    }

    fun getVenues(): ArrayList<Map<String, Any>>? {
        val venueSnapshots = venueCollection.get().get()
        val venues: List<DocumentSnapshot> = venueSnapshots.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        venues.forEach {
            // println(it.data)
            list.add(it.data!!)
        }
        return if (venueSnapshots.size()>0) list else null
    }

    fun getVenue(uid: String): Map<String, Any>? {
        val venueSnapshot = venueCollection.document(uid).get().get()
        return if (venueSnapshot.exists()) venueSnapshot.data else null
    }
}
