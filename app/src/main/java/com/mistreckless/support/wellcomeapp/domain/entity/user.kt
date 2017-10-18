package com.mistreckless.support.wellcomeapp.domain.entity

import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.io.Serializable

/**
 * Created by @mistreckless on 05.08.2017. !
 */


sealed class RegistryUserState

class AlreadyRegisteredState : RegistryUserState()
data class NewUserState(var uid: String, var fullName: String?, var photoUrl: String?) : RegistryUserState(), Serializable
data class ErrorState(var message: String) : RegistryUserState()


data class UserData(var id: String,
                    var ref: String,
                    var cityName: String,
                    var fullName: String?,
                    var displayedName: String,
                    var photoUrl: String?,
                    var likeCount : Long=0,
                    var postCount : Long = 0,
                    var willComeCount : Long = 0,
                    var additionalPoints : Long =0,
                    var generalRating : Long = 0)
//    : Persistable {
//    constructor() : this("","","","","","","")
//
//    override fun writeExternal(out: DataOutput?) {
//        out?.writeString(id)
//        out?.writeString(ref)
//        out?.writeString(ratingRef)
//        out?.writeString(cityName)
//        out?.writeString(fullName)
//        out?.writeString(displayedName)
//        out?.writeString(photoUrl)
//    }
//
//    override fun readExternal(`in`: DataInput?) {
//        id = `in`?.readString()!!
//        ref = `in`.readString()
//        ratingRef = `in`.readString()
//        cityName = `in`.readString()
//        fullName = `in`.readString()
//        displayedName = `in`.readString()
//        photoUrl = `in`.readString()
//    }
//
//    override fun deepClone(): Persistable = copy(id = id, ref = ref)
//
//
//}