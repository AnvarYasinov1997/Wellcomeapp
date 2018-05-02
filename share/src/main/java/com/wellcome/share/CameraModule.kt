package com.wellcome.share

import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.share.picture.PictureSettings
import com.wellcome.share.preview.Preview
import com.wellcome.share.share.Share
import com.wellcome.utils.ui.PerFragment
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