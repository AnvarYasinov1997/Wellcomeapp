package com.mistreckless.support.wellcomeapp.ui.screen.drawer

import android.support.v7.widget.LinearLayoutManager
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.model.DrawerItem
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.view.drawer.DrawerAdapter
import kotlinx.android.synthetic.main.fragment_drawer.*
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */

@Layout(R.layout.fragment_drawer)
class Drawer : BaseFragment<DrawerPresenter, DrawerPresenterProviderFactory>(), DrawerView {

    @Inject
    lateinit var adapter: DrawerAdapter

    override fun getCurrentToolbar() = null

    override fun initUi(drawerItems: List<DrawerItem>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.items.addAll(drawerItems)
        adapter.notifyDataSetChanged()
    }

}


interface DrawerView : BaseFragmentView {
    fun initUi(drawerItems: List<DrawerItem>)

}