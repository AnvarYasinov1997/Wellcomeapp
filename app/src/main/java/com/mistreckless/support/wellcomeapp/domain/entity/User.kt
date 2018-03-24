package com.mistreckless.support.wellcomeapp.domain.entity

data class UserData(
    val id: String = "",
    val ref: String = "",
    val cityName: String = "",
    val displayedName: String = "",
    val photoUrl: String? = "",
    val likeCount: Long = 0,
    val postCount: Long = 0,
    val willComeCount: Long = 0,
    val additionalPoints: Long = 0,
    val generalRating: Long = 0
)
