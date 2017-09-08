package com.mistreckless.support.wellcomeapp.ui.screen.camera

import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by @mistreckless on 03.09.2017. !
 */
@Module
abstract class CameraActivityFragmentProvider{
    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideWallFactory() : Wall
}