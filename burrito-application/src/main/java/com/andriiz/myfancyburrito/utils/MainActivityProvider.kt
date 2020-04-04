package com.andriiz.myfancyburrito.utils

import com.andriiz.domain.contract.Provider
import com.andriiz.myfancyburrito.ui.MainActivity

class MainActivityProvider(private val activity: MainActivity) : Provider<MainActivity> {
    override fun get() = activity
}