package main.kotlin.com.supplier.championleague.model.serializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.geojson.GeoJsonWriter

class GeometrySerializer : JsonSerializer<Geometry>() {
    override fun serialize(value: Geometry?, gen: JsonGenerator, serializers: SerializerProvider) {
        val geoJsonWriter = GeoJsonWriter()
        gen.writeRawValue(value?.let { geoJsonWriter.write(it) })
    }
}