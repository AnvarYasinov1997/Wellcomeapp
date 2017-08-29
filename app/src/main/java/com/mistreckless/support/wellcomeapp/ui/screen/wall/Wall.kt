package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout

/**
 * Created by @mistreckless on 27.08.2017. !
 */

@Layout(R.layout.fragment_wall)
class Wall : BaseFragment<WallPresenter>(), WallView{

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.recycler_view)
    lateinit var recyclerView : RecyclerView

    override fun getCurrentToolbar(): Toolbar? {
        toolbar.title=getString(R.string.app_name)
        return toolbar
    }

    companion object {
        const val TAG="Wall"
    }
}


interface WallView : BaseFragmentView{

}