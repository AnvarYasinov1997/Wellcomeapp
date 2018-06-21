package com.wellcome.share

import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class CameraModule {

    @Provides
    fun provideRxPermissions(cameraActivity: CameraActivity): RxPermissions = RxPermissions(cameraActivity)

}