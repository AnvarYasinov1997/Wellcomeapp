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
    override fun toString(): String {
        return "{ " +
                if (id.isNotEmpty()) "\"$ID\":$id," else "\"$ID\":\"\"," +
                if (ref.isNotEmpty()) "\"$REF\":$ref," else "\"$REF\":\"\"," +
                if (cityName.isNotEmpty()) "\"$CITY_NAME\":$cityName," else "\"$CITY_NAME\":\"\"," +
                if (displayedName.isNotEmpty()) "\"$DISPLAYED_NAME\":$displayedName," else "\"$DISPLAYED_NAME\":\"\"," +
                if (photoUrl?.isNotEmpty() == true)"\"$PHOTO_URL\":$photoUrl," else "\"$PHOTO_URL\":\"\","+
                "\"$LIKE_COUNT\":$likeCount," +
                "\"$POST_COUNT\":$postCount," +
                "\"$WILLCOME_COUNT\":$willComeCount," +
                "\"$ADDITIONAL_POINTS\":$additionalPoints," +
                "\"$GENERAL_RATING\":$generalRating" +
                "}"

    }
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
