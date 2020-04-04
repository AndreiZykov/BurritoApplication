package com.andriiz.myfancyburrito.ui.businesslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.fragmentViewModel
import com.andriiz.domain.data.Business
import com.andriiz.myfancyburrito.R
import com.andriiz.myfancyburrito.R.string
import com.andriiz.myfancyburrito.ui.core.listfragment.GenericListFragment
import com.andriiz.myfancyburrito.ui.listitem.BusinessListItem
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class BusinessFragment : GenericListFragment<Business>() {

    override val emptyListMessage: String
        get() = getString(string.empty_list)

    override val viewModel: BusinessViewModel by fragmentViewModel()

    override val toListItem: (Business) -> Item<GroupieViewHolder>
            = { BusinessListItem(it) { id -> mainActivity.goToMapFragment(id) } }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.burrito_places))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onBackPressed(): Boolean = false

}