package com.andriiz.myfancyburrito.ui.screen.map

import arrow.core.Option
import com.airbnb.mvrx.MvRxState
import com.andriiz.domain.data.Business

data class MapViewState(val business: Option<Business>) : MvRxState