package com.andriiz.myfancyburrito.ui.core

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.andriiz.myfancyburrito.BuildConfig

abstract class MvRxViewModel<S : MvRxState>(initialState: S) : BaseMvRxViewModel<S>(initialState, debugMode = BuildConfig.DEBUG)