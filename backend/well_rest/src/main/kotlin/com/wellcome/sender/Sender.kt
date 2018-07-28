package com.wellcome.sender

interface Sender<T> {

    fun send(dto: T)

}