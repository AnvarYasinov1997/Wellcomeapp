package com.mistreckless.support.wellcomeapp.ui.screen.drawer

import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import dagger.Module
import dagger.Provides

/**
 * Created by @mistreckless on 02.09.2017. !
 */
@Module
class DrawerModule{

    @PerFragment
    @Provides
    fun provideDrawerView(drawer: Drawer) : BaseFragmentView=drawer
}