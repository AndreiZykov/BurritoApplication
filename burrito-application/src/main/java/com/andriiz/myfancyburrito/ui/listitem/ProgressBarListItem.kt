package com.andriiz.myfancyburrito.ui.listitem

import com.andriiz.myfancyburrito.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ProgressBarListItem : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.list_item_progress_bar

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {}
}