package js.externals.firebase.admin.FirebaseFirestore

external interface DocumentData {
}
@Suppress("NOTHING_TO_INLINE")
inline operator fun DocumentData.get(field: String): Any? = asDynamic()[field]

@Suppress("NOTHING_TO_INLINE")
inline operator fun DocumentData.set(field: String, value: Any) {
    asDynamic()[field] = value
}