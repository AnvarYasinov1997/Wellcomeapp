package com.mistreckless.support.wellcomeapp.ui.screen.drawer

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.model.DrawerItem
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.view.drawer.DrawerAdapter
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */

@Layout(R.layout.fragment_drawer)
class Drawer : BaseFragment<DrawerPresenter>(), DrawerView {

    @Inject
    lateinit var adapter: DrawerAdapter

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

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