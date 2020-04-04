package com.andriiz.myfancyburrito.ui.core

import android.view.MenuItem
import com.airbnb.mvrx.BaseMvRxFragment
import com.andriiz.myfancyburrito.ui.MainActivity


abstract class BaseFragment : BaseMvRxFragment() {

    protected val mainActivity: MainActivity
        get() = (requireActivity() as MainActivity)

    abstract fun onBackPressed(): Boolean

    protected fun setToolbarTitle(title: String) {
        mainActivity.supportActionBar?.title = title
    }

    protected fun showBackButton(show: Boolean) {
        with(mainActivity) {
            setHasOptionsMenu(show)
            supportActionBar?.setDisplayHomeAsUpEnabled(show)
            supportActionBar?.setDisplayShowHomeEnabled(show)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home ->  {
                mainActivity.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}