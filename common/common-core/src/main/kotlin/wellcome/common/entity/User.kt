package wellcome.common.entity

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
){
    override fun toString() = "{ " +
                "\"$ID\":\"$id\"," +
                "\"$REF\":\"$ref\"," +
                "\"$CITY_NAME\":\"$cityName\"," +
                "\"$DISPLAYED_NAME\":\"$displayedName\"," +
                "\"$PHOTO_URL\":\"$photoUrl\"," +
                "\"$LIKE_COUNT\":$likeCount," +
                "\"$POST_COUNT\":$postCount," +
                "\"$WILLCOME_COUNT\":$willComeCount," +
                "\"$ADDITIONAL_POINTS\":$additionalPoints," +
                "\"$GENERAL_RATING\":$generalRating" +
                "}"


    companion object {
        const val ID="id"
        const val REF = "ref"
        const val CITY_NAME = "cityName"
        const val DISPLAYED_NAME = "displayedName"
        const val PHOTO_URL = "photoUrl"
        const val LIKE_COUNT = "likeCount"
        const val POST_COUNT = "postCount"
        const val WILLCOME_COUNT = "willComeCount"
        const val ADDITIONAL_POINTS = "additionalPoints"
        const val GENERAL_RATING = "generalRating"

    }
}
