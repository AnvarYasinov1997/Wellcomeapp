package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import android.support.v7.widget.Toolbar
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout

/**
 * Created by @mistreckless on 10.09.2017. !
 */
@Layout(id = R.layout.fragment_preview)
class Preview : BaseFragment<PreviewPresenter,PreviewPresenterProviderFactory>(),PreviewView{
    override fun getCurrentToolbar(): Toolbar? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface PreviewView : BaseFragmentView