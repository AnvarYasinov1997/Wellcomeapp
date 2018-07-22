package com.wellcome.share

import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.ui.core.PerActivity
import dagger.Module
import dagger.Provides
import wellcome.common.core.repository.EventRepository
import wellcome.common.core.repository.UserRepository
import wellcome.common.core.service.LocationService
import wellcome.common.share.ShareInteractor
import wellcome.common.share.ShareInteractorImpl

@Module
class CameraModule {

    @Provides
    fun provideRxPermissions(cameraActivity: CameraActivity): RxPermissions =
        RxPermissions(cameraActivity)

    @PerActivity
    @Provides
    fun provideShareInteractor(locationService: LocationService,
                               eventRepository: EventRepository,
                               userRepository: UserRepository): ShareInteractor =
        ShareInteractorImpl(locationService, userRepository, eventRepository)
}