package com.mistreckless.support.wellcomeapp.domain.entity

/**
 * Created by @mistreckless on 14.10.2017. !
 */

sealed class ShareState

class StateInit : ShareState()
class StateUpload(val progress : Int = 0) : ShareState()
class StateUploaded(val url : String) : ShareState()
class StateDone() : ShareState()
class StateError(val message : String) : ShareState()


data class PostData(val ref : String,
                    val contents: MutableList<ContentData>,
                    val latLon : Pair<Double,Double>,
                    val address : String,
                    val city : String,
                    val isDressControl : Boolean,
                    val ageControl : String,
                    val likeCount : Int =0,
                    val commentCount : Int =0,
                    val willcomeCount : Int = 0,
                    val reportCount : Int =0)


enum class PostType{
    PHOTO,VIDEO
}

data class ContentData(val postType: PostType,
                       val userRef : String,
                       val content : String,
                       val desc : String,
                       val createTime : Long,
                       val deleteTime : Long)
