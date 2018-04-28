package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.view.RealTimeAdapterDelegate
import com.mistreckless.support.wellcomeapp.ui.view.event.SingleEventPresenterProvider
import com.mistreckless.support.wellcomeapp.ui.view.indy.observeScroll
import kotlinx.android.synthetic.main.fragment_wall.*
import javax.inject.Inject

@Layout(R.layout.fragment_wall)
class Wall : BaseFragment<WallPresenter>(), WallView {

    @InjectPresenter
    override lateinit var presenter: WallPresenter

    @ProvidePresenter
    fun providePresenter(): WallPresenter = presenterProvider.get()

    @Inject
    lateinit var singlePresenterProvider: SingleEventPresenterProvider
    @Inject
    lateinit var viewModel: WallViewModel

    private val adapter by lazy {
        WallAdapter(
            RealTimeAdapterDelegate(lifecycle),
            singlePresenterProvider,
            viewModel
        )
    }

    override fun initUi() {
        toolbar.title = getString(R.string.app_name)
        fub.setOnClickListener { presenter.fubClicked() }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        presenter.controlWall(recyclerView.observeScroll())
    }

    companion object {
        const val TAG = "Wall"
    }
}


interface WallView : BaseFragmentView {

    fun initUi()
}