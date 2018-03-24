package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeViewHolder
import com.mistreckless.support.wellcomeapp.ui.view.DelegateRealTimeViewHolder
import com.mistreckless.support.wellcomeapp.ui.view.RealTimeAdapter
import com.mistreckless.support.wellcomeapp.ui.view.event.SingleEventPresenterProvider
import com.mistreckless.support.wellcomeapp.ui.view.event.SingleEventViewHolder

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class WallAdapter(
    private val realTimeAdapter: RealTimeAdapter<BaseRealTimeViewHolder>,
    private val singlePresenterProvider: SingleEventPresenterProvider
) : RecyclerView.Adapter<BaseRealTimeViewHolder>(),
    RealTimeAdapter<BaseRealTimeViewHolder> by realTimeAdapter {

    val events by lazy { mutableListOf<EventData>() }

    override fun getItemViewType(position: Int): Int = SINGLE_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRealTimeViewHolder =
        SingleEventViewHolder(
            DelegateRealTimeViewHolder(
                parent,
                R.layout.view_single_event, singlePresenterProvider
            )
        ).apply { view = this }


    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: BaseRealTimeViewHolder, position: Int) {
        (holder as SingleEventViewHolder).delegate.bind(events[position])
    }

    companion object {
        const val SINGLE_TYPE = 1
    }
}
