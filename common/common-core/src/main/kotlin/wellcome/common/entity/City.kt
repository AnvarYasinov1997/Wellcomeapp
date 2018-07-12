package wellcome.common.entity

data class CityData(val ref: String = "", val name: String = "", val zoneId: String = "") {

    override fun toString() = "{ " +
            "\"$REF\":\"$ref\"," +
            "\"$NAME\":\"$name\"," +
            "\"$ZONE_ID\":\"$zoneId\"," +
            "}"

    companion object {
        const val REF = "ref"
        const val NAME = "name"
        const val ZONE_ID = "zoneId"
    }
}