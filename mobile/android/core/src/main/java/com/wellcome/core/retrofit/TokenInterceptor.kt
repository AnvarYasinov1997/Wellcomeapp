package com.wellcome.core.retrofit

import com.wellcome.core.Cache
import okhttp3.Interceptor
import okhttp3.Response
import wellcome.common.core.CacheConst

class TokenInterceptor(private val cache: Cache) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = cache.getString(CacheConst.FIREBASE_TOKEN,"")
        val builder = chain.request().newBuilder()
        builder.addHeader("token", token)
        return chain.proceed(builder.build())
    }
}