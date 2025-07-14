package com.cairosquad.entity

data class Episode(
    val id: Long,
    val episodeNumber: Int,
    val photoPath: String,
    val episodeName: String,
    val runtimeMinutes: Int,
    val rating: Float,
    val seasonNumber: Int,
    val seriesId: Long,
)
