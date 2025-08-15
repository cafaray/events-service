package main.kotlin.com.supplier.championleague.service

import com.supplier.championleague.model.serializer.VenueResponse
import com.supplier.championleague.repositories.VenueSearchPosRepository
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.repositories.VenuePositionRepository
import main.kotlin.com.supplier.championleague.repositories.VenueRepository

@ApplicationScoped
class VenueService (
    val venueRepository: VenueRepository,
    val venuePositionRepository: VenuePositionRepository,
    val venueSearchPosRepository: VenueSearchPosRepository) {

    fun getVenues(): ArrayList<Map<String, Any>>? =
        venueRepository.getVenues()

    fun getVenueByPosition(lat:Double, long: Double) =
        venuePositionRepository.findByLocation(lat,long)

    fun findNearby(lat: Double, long: Double, maxDistance: Int): List<VenueResponse> {
        return venueSearchPosRepository.getNearby(lat, long, maxDistance)
    }
}
