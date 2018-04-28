package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeViewHolder
import com.mistreckless.support.wellcomeapp.ui.view.DelegateRealTimeViewHolder
import com.mistreckless.support.wellcomeapp.ui.view.RealTimeAdapter
import com.mistreckless.support.wellcomeapp.ui.view.event.SingleEventPresenterProvider
import com.mistreckless.support.wellcomeapp.ui.view.event.SingleEventViewHolder

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class WallAdapter(
    private val realTimeAdapter: RealTimeAdapter<BaseRealTimeViewHolder>,
    private val singlePresenterProvider: SingleEventPresenterProvider,
    private val viewModel: WallViewModel
) : RecyclerView.Adapter<BaseRealTimeViewHolder>(),
    RealTimeAdapter<BaseRealTimeViewHolder> by realTimeAdapter {

    init {
        viewModel.observeState().subscribe(this::stateAction)
    }

    override fun getItemViewType(position: Int): Int = SINGLE_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRealTimeViewHolder =
        SingleEventViewHolder(
            DelegateRealTimeViewHolder(
                parent,
                R.layout.view_single_event, singlePresenterProvider
            )
        ).apply { view = this }


    override fun getItemCount(): Int = viewModel.items.size

    override fun onBindViewHolder(holder: BaseRealTimeViewHolder, position: Int) {
        (holder as SingleEventViewHolder).delegate.bind(viewModel.items[position])
    }

    private fun stateAction(state: ItemState) {
        when (state) {
            is ItemInserted -> notifyItemInserted(state.position)
            is ItemRemoved -> notifyItemRemoved(state.position)
            is ItemChanged -> notifyItemChanged(state.position)
            is ItemRangeInserted -> notifyItemRangeInserted(state.position, state.count)
        }
    }

    companion object {
        const val SINGLE_TYPE = 1
    }
}
