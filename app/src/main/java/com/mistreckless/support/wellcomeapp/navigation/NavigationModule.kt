package com.mistreckless.support.wellcomeapp.navigation

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

/**
 * Created by mistreckless on 21.11.17.
 */

@Singleton
@Module
class NavigationModule{

    @Singleton
    @Provides
    fun provideCicerone() : Cicerone<Router> = Cicerone.create()

    @Singleton
    @Provides
    fun provideRouter(cicerone: Cicerone<Router>) : Router = cicerone.router

    @Singleton
    @Provides
    fun provideNavigationHolder(cicerone: Cicerone<Router>) : NavigatorHolder = cicerone.navigatorHolder
}