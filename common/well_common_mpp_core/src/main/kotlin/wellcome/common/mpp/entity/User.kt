package wellcome.common.mpp.entity

data class UserData(
    val googleUid: String = "",
    val firestoreRef: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val email: String = "",
    val localityFirestoreRef: String = "",
    val localityName: String = ""
) {

    companion object {
        const val GOOGLE_UID = "googleUid"
        const val FIRESTORE_REF = "firestoreRef"
        const val LOCALITY_FIRESTORE_REF = "localityFirestoreRef"
        const val LOCALITY_NAME = "localityName"
        const val NAME = "name"
        const val PHOTO_URL = "photoUrl"
        const val EMAIL = "email"
    }
}
