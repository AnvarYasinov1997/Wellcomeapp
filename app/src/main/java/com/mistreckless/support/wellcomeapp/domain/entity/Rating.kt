package com.mistreckless.support.wellcomeapp.domain.entity

import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * Created by @mistreckless on 10.08.2017. !
 */
data class RatingData(var likeCount : Long=0,
                      var postCount : Long = 0,
                      var willComeCount : Long = 0,
                      var additionalPoints : Long =0,
                      var generalRating : Long = 0) : Persistable{
    override fun writeExternal(out: DataOutput?) {
        out?.writeLong(likeCount)
        out?.writeLong(postCount)
        out?.writeLong(willComeCount)
        out?.writeLong(additionalPoints)
        out?.writeLong(generalRating)
    }

    override fun readExternal(`in`: DataInput?) {
        likeCount=`in`!!.readLong()
        postCount= `in`.readLong()
        willComeCount= `in`.readLong()
        additionalPoints= `in`.readLong()
        generalRating= `in`.readLong()
    }

    override fun deepClone(): Persistable = copy(likeCount = likeCount, postCount = postCount, willComeCount = willComeCount, additionalPoints = additionalPoints, generalRating = generalRating)

}