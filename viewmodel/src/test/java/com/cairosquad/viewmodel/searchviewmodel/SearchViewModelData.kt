package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series


val movie1 = Movie(
    id = 1,
    title = "The dark knight",
    rating = 4.0f,
    posterPath = "/img.jpg"
)

val movie2 = Movie(
    id = 2,
    title = "Girl",
    rating = 4.5f,
    posterPath = "/img.jpg"
)

val series = Series(
    id = 1,
    title = "Series",
    rating = 3.5f,
    posterPath = "/img.jpg"
)

val artist = Artist(
    id = 1,
    name = "Artist",
    photoPath = "/img.jpg"
)