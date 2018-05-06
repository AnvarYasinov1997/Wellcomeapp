package com.wellcome.event

import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.wellcome.event.view.SingleEventPresenterProvider
import com.wellcome.utils.ui.BaseFragment
import com.wellcome.utils.ui.BaseFragmentView
import com.wellcome.utils.ui.RealTimeAdapterDelegate
import com.wellcome.utils.ui.observeScroll
import kotlinx.android.synthetic.main.fragment_wall.*
import kotlinx.coroutines.experimental.Job
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
}


interface WallView : BaseFragmentView {
    fun initUi()
}