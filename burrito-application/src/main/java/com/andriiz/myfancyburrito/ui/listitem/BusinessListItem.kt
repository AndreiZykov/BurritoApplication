package com.andriiz.myfancyburrito.ui.listitem

import com.andriiz.domain.data.Business
import com.andriiz.domain.data.info
import com.andriiz.myfancyburrito.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.list_item_business.view.*
import java.util.*

class BusinessListItem(private val business: Business, private val onClickListener: (String) -> Unit = {} ) : Item<GroupieViewHolder>() {

    override fun getId(): Long =
        UUID.nameUUIDFromBytes(business.id.toByteArray()).mostSignificantBits

    override fun getLayout() = R.layout.list_item_business

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.itemView){
            placeTitle.text = business.name
            placeAddress.text = business.address
            placeInfo.text = business.info

            setOnClickListener { onClickListener(business.id) }
        }
    }

    override fun getChangePayload(newItem: Item<*>): Any? = business


}