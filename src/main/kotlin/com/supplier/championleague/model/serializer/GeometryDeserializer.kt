package main.kotlin.com.supplier.championleague.model.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.geojson.GeoJsonReader

class GeometryDeserializer : JsonDeserializer<Geometry>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Geometry {
        val node: JsonNode = p.codec.readTree(p) // Read as JsonNode
        val geoJson = node.toString() // Convert JsonNode to String
        return GeoJsonReader().read(geoJson) // Parse as GeoJSON
    }
}