package com.supplier.championleague.model

data class EventQueryAttendees(
    val userId: String,
    val timestamp: String,
    val recorded: Boolean = false
)