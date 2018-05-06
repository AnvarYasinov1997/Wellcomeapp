package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.R.id.toolbar
import com.wellcome.utils.ui.BaseFragment
import com.wellcome.utils.ui.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.view.RealTimeAdapterDelegate
import com.mistreckless.support.wellcomeapp.ui.view.event.SingleEventPresenterProvider
import com.mistreckless.support.wellcomeapp.ui.view.indy.observeScroll
import kotlinx.android.synthetic.main.fragment_wall.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class Wall : BaseFragment<WallPresenter>(), WallView {
    override val layoutId: Int
        get() = R.layout.fragment_wall

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
        launch {
            val job = Job()
            presenter.controlWall(recyclerView.observeScroll(job))

        }
    }
    companion object {
        const val TAG = "Wall"
    }
}


interface WallView : BaseFragmentView {
    fun initUi()
}