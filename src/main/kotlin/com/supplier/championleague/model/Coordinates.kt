package main.kotlin.com.supplier.championleague.model

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection // ✅ Helps Quarkus detect this class at runtime
data class Coordinates(
    val lat: Number,
    val long: Number
)