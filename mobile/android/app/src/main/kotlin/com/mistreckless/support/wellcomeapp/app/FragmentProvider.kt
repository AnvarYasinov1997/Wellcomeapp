package com.mistreckless.support.wellcomeapp.app

import com.wellcome.event.Wall
import com.wellcome.event.WallModule
import com.wellcome.share.picture.PictureSettings
import com.wellcome.share.preview.Preview
import com.wellcome.share.share.Share
import com.wellcomeapp.ui_core.PerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentProvider {

    @ContributesAndroidInjector(modules = [WallModule::class])
    @PerFragment
    abstract fun provideWallFactory(): Wall
}

@Module
abstract class CameraActivityFragmentProvider {
    @ContributesAndroidInjector
    @PerFragment
    abstract fun providePreviewFactory(): Preview

    @ContributesAndroidInjector
    @PerFragment
    abstract fun providePictureSettings(): PictureSettings

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideShare(): Share
}