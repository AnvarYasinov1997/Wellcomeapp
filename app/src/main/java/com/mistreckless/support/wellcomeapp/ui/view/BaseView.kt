//package com.mistreckless.support.wellcomeapp.ui.view
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import butterknife.ButterKnife
//import butterknife.Unbinder
//import com.mistreckless.support.wellcomeapp.ui.BasePresenter
//import com.mistreckless.support.wellcomeapp.ui.screen.Layout
//
///**
// * Created by @mistreckless on 27.08.2017. !
// */
//abstract class BaseView<P : BasePresenter<*, *>, M, R>(parent: ViewGroup?) : FrameLayout(parent?.context) {
//
//    var unbinder: Unbinder? = null
//    abstract var presenter : P
//    abstract var router : R
//
//    init {
//        val cls = javaClass
//        if (cls.isAnnotationPresent(Layout::class.java)) {
//            val annotation = cls.getAnnotation(Layout::class.java)
//            val view = LayoutInflater.from(context).inflate(annotation.id, this)
//            unbinder = ButterKnife.bind(this, view)
//        }
//    }
//
//    fun bind(item: M){
//        presenter.attachView(this)
//        presenter.attachRouter(router as Any)
//    }
//    fun unbind(){
//        presenter.detachView()
//        presenter.detachRouter()
//    }
//}