package com.wellcome.wellcomeapp.navigation

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class NavigationModule{

    private val cicerone : Cicerone<Router> by lazy { Cicerone.create() }

    @Singleton
    @Provides
    fun provideRouter() : Router = cicerone.router

    @Singleton
    @Provides
    fun provideNavigationHolder() : NavigatorHolder = cicerone.navigatorHolder
}