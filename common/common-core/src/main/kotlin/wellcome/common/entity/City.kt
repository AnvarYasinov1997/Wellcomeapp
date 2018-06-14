package wellcome.common.entity

data class CityData(val ref: String = "", val name: String = "", val zoneId: String = "") {

    companion object {
        const val REF = "ref"
        const val NAME = "name"
        const val ZONE_ID = "zoneId"
    }
}