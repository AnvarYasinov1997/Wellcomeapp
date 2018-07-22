package com.wellcome.wellcomeapp.app

import dagger.Module
import dagger.Provides
import wellcome.common.core.Cache
import wellcome.common.core.repository.EventRepository
import wellcome.common.core.repository.EventRepositoryImpl
import wellcome.common.core.repository.UserRepository
import wellcome.common.core.repository.UserRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(cache: Cache): UserRepository = UserRepositoryImpl(cache)

    @Singleton
    @Provides
    fun provideEventRepository(cache: Cache): EventRepository = EventRepositoryImpl(cache)

}
