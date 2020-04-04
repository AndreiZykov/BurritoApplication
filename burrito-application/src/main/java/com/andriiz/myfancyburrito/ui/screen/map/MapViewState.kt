package com.andriiz.myfancyburrito.ui.screen.map

import com.airbnb.mvrx.MvRxState
import com.andriiz.domain.data.Business

data class MapViewState(val business: Business?) : MvRxState