package main.kotlin.com.supplier.championleague.service

import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.repositories.VenuePositionRepository
import main.kotlin.com.supplier.championleague.repositories.VenueRepository

@ApplicationScoped
class VenueService (
    val venueRepository: VenueRepository,
    val venuePositionRepository: VenuePositionRepository) {

    fun getVenues(): ArrayList<Map<String, Any>>? =
        venueRepository.getVenues()

    fun getVenueByPosition(lat:Double, long: Double) =
        venuePositionRepository.findByLocation(lat,long)
}
