package com.wellcome.event

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.wellcome.event.view.SingleEventPresenterProvider
import com.wellcome.event.view.SingleEventViewHolder
import com.wellcome.core.ui.RealTimeAdapter
import com.wellcome.core.ui.RealTimeViewHolder

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class WallAdapter(
    private val realTimeAdapter: RealTimeAdapter<RealTimeViewHolder<*, *, *>>,
    private val singlePresenterProvider: SingleEventPresenterProvider,
    private val viewModel: WallViewModel
) : RecyclerView.Adapter<RealTimeViewHolder<*, *, *>>(),
    RealTimeAdapter<RealTimeViewHolder<*, *, *>> by realTimeAdapter {

    init {
        viewModel.observeState().subscribe(this::stateAction)
    }

    override fun getItemViewType(position: Int): Int =
        SINGLE_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealTimeViewHolder<*, *, *> =
        SingleEventViewHolder(
            R.layout.view_single_event,
            parent,
            singlePresenterProvider
        )


    override fun getItemCount(): Int = viewModel.items.size

    override fun onBindViewHolder(holder: RealTimeViewHolder<*, *, *>, position: Int) {
        (holder as SingleEventViewHolder).bind(viewModel.items[position])
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
