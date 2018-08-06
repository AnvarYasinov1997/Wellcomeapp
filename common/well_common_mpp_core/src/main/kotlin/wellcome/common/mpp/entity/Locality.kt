package wellcome.common.mpp.entity

data class LocalityData(val ref: String = "", val name: String = "", val timezoneId: String = "") {

    companion object {
        const val REF = "ref"
        const val NAME = "name"
        const val TIMEZONE_ID = "timezoneId"
    }
}