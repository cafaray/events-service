package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Type
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
@RegisterForReflection // Helps Quarkus detect this class at runtime
@Entity
@Table(name = "stadiums")
data class VenuePosition (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "location", columnDefinition = "geometry")
    @Type(type = "org.hibernate.spatial.JTSGeometryType")
    var location: Point? = null,

    @Column(name = "area", columnDefinition = "geometry")
    @Type(type = "org.hibernate.spatial.JTSGeometryType")
    var area: Polygon? = null
)
