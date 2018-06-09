package com.wellcome.event

import android.arch.lifecycle.ViewModelProviders
import com.wellcome.core.ui.PerFragment
import dagger.Module
import dagger.Provides

@PerFragment
@Module
class WallModule {

    @PerFragment
    @Provides
    fun provideViewModel(wall: Wall): WallViewModel = ViewModelProviders.of(wall)[WallViewModel::class.java]
}