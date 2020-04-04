package com.andriiz.myfancyburrito.ui.listitem

import com.andriiz.myfancyburrito.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.list_item_error.view.*

class ErrorListItem(private val errorMessage: String) : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.list_item_error

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.itemView){ message.text = errorMessage }
    }

}