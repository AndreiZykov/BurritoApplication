package com.andriiz.myfancyburrito.ui.listitem

import android.view.View
import com.andriiz.domain.data.Business
import com.andriiz.domain.data.info
import com.andriiz.myfancyburrito.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.list_item_business.view.*
import java.util.*

class BusinessListItem(private val business: Business, private val onClickListener: (String, View) -> Unit = { _, _ ->} ) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.list_item_business

    override fun getId(): Long =
        UUID.nameUUIDFromBytes(business.id.toByteArray()).mostSignificantBits

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.itemView){
            businessName.text = business.name
            transactionViewBusinessAddress.text = business.address
            businessAddress.text = business.address
            businessInfo.text = business.info
            transactionViewBusinessAddressBusinessInfo.text = business.info

            sharedElement.transitionName = business.id
            setOnClickListener {
                transactionView.animate()
                    .alpha(FULL_ALPHA)
                    .setDuration(GOOD_DURATION_TIME)
                    .withEndAction { onClickListener(business.id, sharedElement) }
                    .start()
            }
        }
    }

    override fun getChangePayload(newItem: Item<*>): Any? = business

    companion object {
        private const val GOOD_DURATION_TIME = 250L
        private const val FULL_ALPHA = 1F
    }

}