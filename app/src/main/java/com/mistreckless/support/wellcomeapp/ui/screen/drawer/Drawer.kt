package com.mistreckless.support.wellcomeapp.ui.screen.drawer

import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.view.drawer.DrawerAdapter
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */

@Layout(R.layout.fragment_drawer)
class Drawer : BaseFragment<DrawerPresenter>(), DrawerView{

    @Inject
    lateinit var adapter : DrawerAdapter

    override fun getCurrentToolbar()=null

}


interface DrawerView : BaseFragmentView{

}