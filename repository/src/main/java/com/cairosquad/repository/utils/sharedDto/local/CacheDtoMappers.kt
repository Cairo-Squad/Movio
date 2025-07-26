package com.cairosquad.repository.utils.sharedDto.local

import com.cairosquad.entity.Review

fun ReviewCacheDto.toCacheDto(): Review {
    return Review(
        id = id,
        author = author,
        authorPhotoPath =  authorPhotoPath,
        rating = rating,
        date = date,
        description = description,
    )
}

fun List<ReviewCacheDto>.toEntityList(): List<Review> {
    return map { it.toCacheDto() }
}

fun Review.toCacheDto(): ReviewCacheDto {
    return ReviewCacheDto(
        id = id,
        author = author,
        authorPhotoPath =  authorPhotoPath,
        rating = rating,
        date = date,
        description = description,
    )
}

fun List<Review>.toRequestWithReviewsCacheDto(request: String): RequestWithReviewsCacheDto {
    return RequestWithReviewsCacheDto(
        request = RequestCacheDto(request = request),
        reviews = this.map { it.toCacheDto() }
    )
}
