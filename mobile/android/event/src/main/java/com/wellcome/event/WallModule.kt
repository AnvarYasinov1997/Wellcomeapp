package com.wellcome.event

import android.arch.lifecycle.ViewModelProviders
import com.wellcomeapp.ui_core.PerFragment
import dagger.Module
import dagger.Provides

@Module
class WallModule {

    @PerFragment
    @Provides
    fun provideViewModel(wall: Wall): WallViewModel = ViewModelProviders.of(wall)[WallViewModel::class.java]
}