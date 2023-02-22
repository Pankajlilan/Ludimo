package com.pankaj.ludimos.model

data class BallTracking(
    val created_at: String = "",
    val height: Int = 0,
    val rotation: String = "",
    val size: String = "",
    val source_url: String = "",
    val stump_line: List<Float> = listOf(),
    val thumbnail_url: String = "",
    val title: String = "",
    val training_date: String = "",
    val updated_at: String = "",
    val user: User = User(),
    val width: Int = 0
)