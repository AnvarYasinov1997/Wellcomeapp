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
                    val postType: PostType,
                    val contents: MutableList<Pair<String,PostType>>,
                    val users : MutableList<String>,
                    val latLon : Pair<Double,Double>,
                    
                    )


enum class PostType{
    PHOTO,VIDEO
}
