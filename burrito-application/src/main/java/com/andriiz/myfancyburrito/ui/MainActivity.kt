package com.andriiz.myfancyburrito.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.andriiz.domain.contract.LocationProvider
import com.andriiz.domain.contract.Provider
import com.andriiz.myfancyburrito.R
import com.andriiz.myfancyburrito.ui.core.BaseFragment
import com.andriiz.myfancyburrito.ui.screen.businesslist.BusinessFragment
import com.andriiz.myfancyburrito.ui.screen.map.MapFragment
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

    fun goToMapFragment(id: String, sharedView: View) {
        showFragment(MapFragment.instance(id), MAP_FRAGMENT_TAG, sharedView)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            val fragment =
                (supportFragmentManager.findFragmentById(android.R.id.content) as? BaseFragment)
            if (fragment?.onBackPressed() != true) {
                super.onBackPressed()
            }
        } else finish()
    }

    private fun showFragment(fragment: Fragment, tag: String, sharedView: View? = null) {
        if(supportFragmentManager.findFragmentByTag(tag) == null){
            supportFragmentManager.beginTransaction().apply {
                if(sharedView != null) addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView).orEmpty())
            }.replace(android.R.id.content, fragment, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

    companion object {
        private const val BUSINESS_LIST_FRAGMENT_TAG = "BUSINESS_LIST_FRAGMENT_TAG"
        private const val MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG"
    }

}