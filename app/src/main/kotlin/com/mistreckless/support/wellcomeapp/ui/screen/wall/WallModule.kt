package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.arch.lifecycle.ViewModelProviders
import com.wellcome.utils.ui.PerFragment
import dagger.Module
import dagger.Provides

@PerFragment
@Module
class WallModule {

    @PerFragment
    @Provides
    fun provideViewModel(wall: Wall): WallViewModel = ViewModelProviders.of(wall)[WallViewModel::class.java]
}