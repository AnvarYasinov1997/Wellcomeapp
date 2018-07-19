package com.wellcome.event

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.wellcome.event.view.SingleEventPresenterProvider
import com.wellcomeapp.ui_core.*
import kotlinx.android.synthetic.main.fragment_wall.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class Wall : BaseFragment<WallPresenter>(), WallView {
    override val layoutId: Int
        get() = R.layout.fragment_wall
    private val parentContainer by lazy {
        activity as ParentContainer
    }

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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy >0 && fub.visibility == View.VISIBLE) {
                    parentContainer.hideBottom()
                    fub.hide()
                }
                else if(dy<0 && fub.visibility != View.VISIBLE){
                    parentContainer.showBottom()
                    fub.show()
                }
            }
        })
    }
}

interface WallView : BaseFragmentView {
    fun initUi()
}