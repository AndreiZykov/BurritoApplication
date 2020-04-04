package com.andriiz.myfancyburrito.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andriiz.domain.contract.LocationProvider
import com.andriiz.domain.contract.Provider
import com.andriiz.myfancyburrito.R
import com.andriiz.myfancyburrito.ui.businesslist.BusinessFragment
import com.andriiz.myfancyburrito.ui.core.BaseFragment
import com.andriiz.myfancyburrito.ui.mapfragment.MapFragment
import com.andriiz.myfancyburrito.utils.LocationProviderImpl
import com.andriiz.myfancyburrito.utils.MainActivityProvider
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {

    private val managementActivityModule = module {
        factory<Provider<MainActivity>> { MainActivityProvider(this@MainActivity) }
        factory<LocationProvider> { LocationProviderImpl(get()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(managementActivityModule)
        super.onCreate(savedInstanceState)
        showFragment(BusinessFragment(), BUSINESS_LIST_FRAGMENT_TAG)
    }

    override fun onDestroy() {
        unloadKoinModules(managementActivityModule)
        super.onDestroy()
    }

    fun goToMapFragment(id: String) {
        showFragment(MapFragment.instance(id), MAP_FRAGMENT_TAG, animate = true)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) finish()
        val fragment =
            (supportFragmentManager.findFragmentById(android.R.id.content) as? BaseFragment)
        if (fragment?.onBackPressed() == true) return
        super.onBackPressed()
    }

    private fun showFragment(fragment: Fragment, tag: String, animate: Boolean = false) {
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)
        if (existingFragment != null) {
            val bt = supportFragmentManager.beginTransaction()
            bt.show(existingFragment)
            bt.commitNow()
        } else {
            val bt = supportFragmentManager.beginTransaction()
            if(animate)
                bt.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            bt.replace(android.R.id.content, fragment, tag)
            bt.addToBackStack(tag)
            bt.commit()
        }
    }

    companion object {
        private const val BUSINESS_LIST_FRAGMENT_TAG = "BUSINESS_LIST_FRAGMENT_TAG"
        private const val MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG"
    }

}