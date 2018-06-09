package com.mistreckless.support.wellcomeapp.ui

import io.reactivex.Observable
import org.junit.Assert.*
import org.junit.Test

class MainActivityPresenterTest{

    @Test
    fun test(){
        print(Observable.just(7)
            .buffer(3)
            .blockingIterable()
            .first())

    }
}