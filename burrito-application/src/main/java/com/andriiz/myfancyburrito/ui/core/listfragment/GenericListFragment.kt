package com.andriiz.myfancyburrito.ui.core.listfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.withState
import com.andriiz.myfancyburrito.R
import com.andriiz.myfancyburrito.ui.core.BaseFragment
import com.andriiz.myfancyburrito.ui.listitem.ErrorListItem
import com.andriiz.myfancyburrito.ui.listitem.ProgressBarListItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_recycler_view.*

abstract class GenericListFragment<Entity> : BaseFragment() {

    abstract val viewModel: GenericListViewModel<Entity>

    abstract val emptyListMessage: String

    abstract val toListItem: (Entity) -> Item<GroupieViewHolder>

    private val groupAdapter: GroupAdapter<*>
        get() = (recyclerView?.adapter as? GroupAdapter<*>)
            ?: GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, vg: ViewGroup?, b: Bundle?): View =
        inflater.inflate(R.layout.fragment_recycler_view, vg, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }
    
    override fun invalidate() = withState(viewModel) { state ->

        when {
            state.error.nonEmpty() -> listOf(ErrorListItem(getString(state.errorMessage())))
            state.isLoading -> listOf(ProgressBarListItem())
            else -> state.list.map(toListItem)
                .ifEmpty { listOf(ErrorListItem(emptyListMessage)) }
        }.let(groupAdapter::updateAsync)

    }

}