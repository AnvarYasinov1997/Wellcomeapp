package com.mistreckless.support.wellcomeapp.data.auth

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Consumer
import org.junit.Assert.*
import org.junit.Test

class GoogleAuthServiceTest{
    @Test
    fun test() {
        Single.just(emptyList<Unit>())
            .filter(List<Unit>::isEmpty)
            .flatMap { Maybe.just("yes") }
            .switchIfEmpty (Maybe.just(Unit).flatMap { print("suke");Maybe.just("no") })
            .toSingle()
            .subscribe(Consumer{
                   println(it)
            })

    }
}