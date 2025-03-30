package main.kotlin.com.supplier.championleague.repositories

import jakarta.enterprise.context.ApplicationScoped
import io.quarkus.hibernate.orm.panache.PanacheRepository
import main.kotlin.com.supplier.championleague.model.VenuePosition

@ApplicationScoped
class VenuePositionRepository: PanacheRepository<VenuePosition> {

    fun findByLocation(lat: Double, long: Double): List<VenuePosition> {
        // -3.6890, 40.4520
        // ST_DWithin(location, ST_SetSRID(ST_MakePoint(?1, ?2), 4326), ?3)" , radiusMeters
        // val radiusMeters: Double = 1000.0
        val list: List<VenuePosition> = list(
            "ST_Contains(location, ST_SetSRID(ST_Point(?1, ?2), 4326))",
            lat, long
        )
        return list
    }
}
