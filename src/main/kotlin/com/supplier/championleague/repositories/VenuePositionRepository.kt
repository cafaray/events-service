package main.kotlin.com.supplier.championleague.repositories

import jakarta.enterprise.context.ApplicationScoped
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import main.kotlin.com.supplier.championleague.model.VenuePosition

@ApplicationScoped
class VenuePositionRepository: PanacheRepositoryBase<VenuePosition, Long> {

    fun findByLocation(lat: Double, long: Double): List<VenuePosition> {
        // -3.6890, 40.4520
        // ST_DWithin(location, ST_SetSRID(ST_MakePoint(?1, ?2), 4326), ?3)" , radiusMeters
        // val radiusMeters: Double = 1000.0
        println("Search params: lat: $lat, long: $long")
        val list: List<VenuePosition> = find("ST_Contains(location, ST_SetSRID(ST_Point(?1, ?2), 4326))", lat, long).list()
        println("Found ${list.size} venue(s)")
        return list
    }
}
