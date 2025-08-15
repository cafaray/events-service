package main.kotlin.com.supplier.championleague.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.Type
import org.hibernate.type.SqlTypes
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
// import org.hibernate.annotations.Type

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

    //@JdbcTypeCode(SqlTypes.GEOMETRY)
    // @Type(GeometryType::class)
    @JsonIgnore
    @Column(name = "location", columnDefinition = "geometry(Polygon, 4326)")
    var location: Polygon? = null,

    @Column(name = "document_id")
    var documentId: String? = null
)