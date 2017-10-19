package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.view.indy.observeScroll
import kotlinx.android.synthetic.main.fragment_wall.*
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */

@Layout(R.layout.fragment_wall)
class Wall : BaseFragment<WallPresenter, WallPresenterProviderFactory>(), WallView{

    @Inject
    lateinit var adapter : WallAdapter

    override fun getCurrentToolbar(): Toolbar? {
        toolbar.title=getString(R.string.app_name)
        return toolbar
    }

    override fun initUi(){
        fub.setOnClickListener { presenter.fubClicked() }
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter=adapter
        presenter.controlWall(recyclerView.observeScroll())
    }

    companion object {
        const val TAG="Wall"
    }
}


interface WallView : BaseFragmentView{

    fun initUi()
}