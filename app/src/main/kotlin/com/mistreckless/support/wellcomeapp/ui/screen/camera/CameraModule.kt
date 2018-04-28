package com.mistreckless.support.wellcomeapp.ui.screen.camera

import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.screen.camera.picture.PictureSettings
import com.mistreckless.support.wellcomeapp.ui.screen.camera.preview.Preview
import com.mistreckless.support.wellcomeapp.ui.screen.camera.share.Share
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by @mistreckless on 03.09.2017. !
 */
@Module
abstract class CameraActivityFragmentProvider{
    @ContributesAndroidInjector
    @PerFragment
    abstract fun providePreviewFactory() : Preview

    @ContributesAndroidInjector
    @PerFragment
    abstract fun providePictureSettings() : PictureSettings

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideShare() : Share
}

@Module
class CameraModule{

    @Provides
    fun provideRxPermissions(cameraActivity: CameraActivity) : RxPermissions=RxPermissions(cameraActivity)

}